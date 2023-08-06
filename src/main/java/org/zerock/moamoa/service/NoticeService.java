package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.notice.NoticeMapper;
import org.zerock.moamoa.domain.DTO.notice.NoticeReadUpdateRequest;
import org.zerock.moamoa.domain.DTO.notice.NoticeResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.repository.NoticeRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;
    private final UserService userService;
//    private final ProductService productService;
//    private final Emitter emitter;
    private final SimpMessageSendingOperations messagingTemplate;

    public NoticeResponse findOne(Long id) {
        return noticeMapper.toDto(findById(id));
    }

    public Notice findById(Long id) {
        return this.noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND));
    }

    public List<Notice> findAll() {
        return this.noticeRepository.findAll();
    }

    // YJ: db에 추가될때 리스트 추가 + 이벤트리스너가 알림 보내는거 해야됨 각각 메소드 추가하기
    // -> request 받아서 response 내보내는거 하면 될듯!!
    public NoticeResponse saveNotice(NoticeSaveRequest request) {
        Notice notice = noticeMapper.toEntity(request);
//        User user = notice.getReceiverID();
//        notice.setUserNotice(user);

//        NoticeType noticeType = NoticeType.values()[request.getType().getCode()];
//        notice.setType(noticeType);

        Notice savedNotice = noticeRepository.save(notice);

        // receiver에게 알림 발송
        //        sendNotice(savedNotice); // 알림 발송

        // 근데조회는걍... 검색할때마다불러오면되는거잔아 알림만보내고
        // 조회할때마다 Repo에서 페이지형식으로 전부 불러오기.

        return noticeMapper.toDto(savedNotice);
    }

    public void removeNotice(Long id) {
        // 알림 완전삭제 X, 유저의 noticeList에서만 사라지면 될듯
        Optional<Notice> noticeOptional = noticeRepository.findById(id);
//        noticeOptional.ifPresent(Notice::removeUserNotice); // = (notice -> {notice.removeUserNotice();})
    }

    // 읽을 시 상태 변경
    @Transactional
    public NoticeResponse updateRead(NoticeReadUpdateRequest NR) {
        Notice temp = findById(NR.getId());
        temp.updateRead(true);
        return noticeMapper.toDto(temp);
    }

    // 걍 페이지형식으로 확인할때마다 한번에 불러오는게 나을듯?... 나중에 스크롤을하든 어쩌든
    //
    public Page<NoticeResponse> getNotices(Long receiverID, int pageNo, int pageSize) {
        User receiver = userService.findById(receiverID);
        Pageable itemPage =  PageRequest.of(pageNo, pageSize);

        Page<Notice> noticePage = noticeRepository.findByReceiverID(receiver, itemPage);
        if (noticePage.isEmpty()){
            throw new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND);
        }

        return noticePage.map(notice -> findOne(notice.getId()));
    }

    // 알림 발신
    // type 설정, type에 따라 message를 다르게 하자
//    private void sendNotice(Notice notice) {
//        User receiver = notice.getReceiverID();
//        String message = generateNoticeMessage(notice); // 알림 메시지 생성
//
//        // Emitter를 사용하여 알림 발송
//        emitter.sendNotification(receiver.getId(), message);
//    }

    public void sendNotice(NoticeSaveRequest noticeSaveReq) {
        User receiver = userService.findById(noticeSaveReq.getReceiverID());
        Notice notice = noticeMapper.toEntity(noticeSaveReq);
        // 밑에는 String버전, 그 밑에 NoticeResponse 반환하는것도 해봄
//        messagingTemplate.convertAndSend("/sub/" + receiver, generateNoticeMessage(notice));
        messagingTemplate.convertAndSend("/sub/" + receiver, getResponseObject(notice));
        // 로깅 추가
        log.info("Sended WebSocket message to /sub/" + noticeSaveReq.getReceiverID() + " path");
    }

//    private String generateNoticeMessage(Notice notice) {
//        StringBuilder messageBuilder = new StringBuilder();
//
//        // 알림의 타입에 따라 다른 메시지 추가
//        String type = notice.getType();
//        switch (type) {
//            case "post_changed" -> messageBuilder.append("게시글 변경");
//            case "post_status_changed" -> messageBuilder.append("게시글 상태 변경");
//            case "interest_post_changed" -> messageBuilder.append("관심 게시글 변경");
//            case "notice_added" -> messageBuilder.append("새로운 공지사항 추가");
//            case "new_chatroom" -> messageBuilder.append("새로운 채팅방 추가");
//            default -> messageBuilder.append("새 알림");
//        }//이렇게할바에야그냥... enum으로 type따라 다른 메시지 주는게 낫지않나...
//        // 얘를 저장해놓고 접속할 시 알림 불러옴/새 알림 있을시 Emitter로 전송?
//        // 일단 본게 SSE Emitter / 웹소켓/ PubSub
//        // -> 웹소켓+STOMP
//
//
//        // 메시지에 추가적인 정보를 포함시키고 반환
//        User user = notice.getSenderID();               //userService.findById(notice.getSenderID());
//        Product product = (notice.getReferenceID());    //productService.findById(notice.getReferenceID());
//
//        messageBuilder.append(" - Sender: ").append(user.getNick());
//        messageBuilder.append(", Reference Title: ").append(product.getTitle());
//
//        return messageBuilder.toString();
//    }

    public NoticeResponse getResponseObject(Notice notice) {
        return noticeMapper.toDto(notice);
    }
}
