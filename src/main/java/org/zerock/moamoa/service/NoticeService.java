package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public NoticeResponse saveNotice(NoticeSaveRequest request, Long uid) {
        Notice notice = noticeMapper.toEntity(request);
        User user = notice.getReceiverID();
        notice.setUserNotice(user);

        Notice savedNotice = noticeRepository.save(notice);
//        sendNotice(savedNotice); // 알림 발송

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


    // 알림 조회
    public List<NoticeResponse> getReminderNotices(Long userId) {
        List<Notice> notices = userService.findById(userId).getNotices();
        if (notices.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND);
        } else {
            return noticeMapper.toDtoList(notices);
        }
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
        // 밑에는 Styring버전, 그 밑에 NoticeResponse 반환하는것도 해봄
//        messagingTemplate.convertAndSend("/sub/" + receiver, generateNoticeMessage(notice));
        messagingTemplate.convertAndSend("/sub/" + receiver, getResponseObject(notice));
        // 로깅 추가
        log.info("Sended WebSocket message to /sub/" + noticeSaveReq.getReceiverID() + " path");
    }

    private String generateNoticeMessage(Notice notice) {
        StringBuilder messageBuilder = new StringBuilder();

        // 알림의 타입에 따라 다른 메시지 추가
        String type = notice.getType();
        switch (type) {
            case "post_changed" -> messageBuilder.append("게시글 변경");
            case "post_status_changed" -> messageBuilder.append("게시글 상태 변경");
            case "interest_post_changed" -> messageBuilder.append("관심 게시글 변경");
            case "notice_added" -> messageBuilder.append("새로운 공지사항 추가");
            case "new_chatroom" -> messageBuilder.append("새로운 채팅방 추가");
            default -> messageBuilder.append("새 알림");
        }//이렇게할바에야그냥... enum으로 type따라 다른 메시지 주는게 낫지않나...
        // 얘를 저장해놓고 접속할 시 알림 불러옴/새 알림 있을시 Emitter로 전송?
        // 일단 본게 SSE Emitter / 웹소켓/ PubSub
        // -> 웹소켓+STOMP


        // 메시지에 추가적인 정보를 포함시키고 반환
        User user = notice.getSenderID();               //userService.findById(notice.getSenderID());
        Product product = (notice.getReferenceID());    //productService.findById(notice.getReferenceID());

        messageBuilder.append(" - Sender: ").append(user.getNick());
        messageBuilder.append(", Reference Title: ").append(product.getTitle());

        return messageBuilder.toString();
    }

    public NoticeResponse getResponseObject(Notice notice) {
        return noticeMapper.toDto(notice);
    }
}
