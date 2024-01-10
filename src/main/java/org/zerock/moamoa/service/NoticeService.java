package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.notice.*;
import org.zerock.moamoa.domain.DTO.product.ProductTitleResponse;
import org.zerock.moamoa.domain.DTO.user.UserNickResponse;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.EmitterRepository;
import org.zerock.moamoa.repository.NoticeRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;
    private final UserRepository userRepository;
    private final EmitterRepository emitterRepository;

    public List<Notice> findAll() {
        return this.noticeRepository.findAll();
    }

    @Transactional
    public void saveAndSend(NoticeSaveRequest request) {
        Notice savedNotice = noticeRepository.save(noticeMapper.toEntity(request));
        User senderID = savedNotice.getSenderID();
        Product referenceID = savedNotice.getReferenceID();

        // receiver에게 알림 발송
        Long receiverId = request.getReceiverID().getId();
        String eventId = request.getReceiverID().getId() + "_" + System.currentTimeMillis();

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        for (Map.Entry<String, SseEmitter> entry : sseEmitters.entrySet()) {
            String key = entry.getKey();
            SseEmitter emitter = entry.getValue();
            emitterRepository.saveEventCache(key, request);
            NoticeResponse res = new NoticeResponse(savedNotice.getId(),
                    userToDto(senderID), receiverId, savedNotice.getReadOrNot(), savedNotice.getType(),
                    productToDto(referenceID), savedNotice.getCreatedAt());
            sendToClient(emitter, eventId, key, res);

        }
    }

    private void sendToClient(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    // 페이지형식으로 확인할때마다 한번에 불러오는게 나을듯
    public Page<NoticeResponse> getNotices(String receiverName, int pageNo, int pageSize) {
        User receiver = userRepository.findByEmailOrThrow(receiverName);
        Pageable itemPage = PageRequest.of(pageNo, pageSize);

        Page<Notice> noticePage = noticeRepository.findByReceiverID(receiver, itemPage);

        return noticePage.map(noticeMapper::toDto);
    }

    public ResultResponse removeNotice(String username, Long id) {
        User user = userRepository.findByEmailOrThrow(username);
        Notice notice = noticeRepository.findByIdOrThrow(id);
        if (!notice.getReceiverID().getId().equals(user.getId())) {
            return ResultResponse.toDto("권한이 없습니다.");
        }
        noticeRepository.delete(notice);
        return ResultResponse.toDto("OK");
    }

    // 읽을 시 상태 변경
    @Transactional
    public ResultResponse updateRead(String username, Long nid) {
        User user = userRepository.findByEmailOrThrow(username);
        Notice temp = noticeRepository.findByIdOrThrow(nid);
        if (temp.getReceiverID().getId().equals(user.getId())){
            temp.updateRead(true);
            return ResultResponse.toDto("OK");
        }
        return ResultResponse.toDto("권한이 없습니다.");
    }

    @Transactional
    public ResultResponse updateReadAll(String username) {
        User user = userRepository.findByEmailOrThrow(username);
        List<Notice> notices = noticeRepository.findByReceiverID(user);
        if (notices != null)
            notices.forEach(notice -> notice.updateRead(true));
        return ResultResponse.toDto("OK");
    }

    @Transactional
    public UserNickResponse userToDto(User user) {
        return UserNickResponse.builder()
                .code(user.getCode())
                .nick(user.getNick())
                .build();
    }

    @Transactional
    public ProductTitleResponse productToDto(Product product){
        return ProductTitleResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .productImages(product.getProductImages())
                .build();
    }

}
