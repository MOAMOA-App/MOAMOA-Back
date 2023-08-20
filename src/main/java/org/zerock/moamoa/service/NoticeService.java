package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.notice.NoticeMapper;
import org.zerock.moamoa.domain.DTO.notice.NoticeReadUpdateRequest;
import org.zerock.moamoa.domain.DTO.notice.NoticeResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.EmitterRepository;
import org.zerock.moamoa.repository.NoticeRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;
    private final UserService userService;
    private final EmitterRepository emitterRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;    // SSE 연결은 1시간동안 지속됨

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

    // YJ: 이벤트리스너가 알림 보내는거 해야됨
    public NoticeResponse saveAndSend(NoticeSaveRequest request) {
        Notice savedNotice = noticeRepository.save(noticeMapper.toEntity(request));

        // receiver에게 알림 발송
        Long receiverId = request.getReceiverID();
        String eventId = receiverId + "_" + System.currentTimeMillis();
        System.out.println("Generated eventId: " + eventId);

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, request);
                    sendToClient(emitter, eventId, key, noticeMapper.toDto(savedNotice));
                }
        );

        return noticeMapper.toDto(savedNotice);
    }

    /**
     * SSE 연결
     */
    public SseEmitter subscribe(Long memberId, String lastEventId) {
        String emitterId = makeTimeIncludeId(memberId); // username을 포함하여 SseEmitter를 식별하기 위한 고유 아이디 생성
                                                        // 시간을 emitterId에 붙여두면 데이터가 유실된 시점을 알 수 있음
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(memberId);

        // 503 에러 방지 위해 최초 연결 시 더미 이벤트 전송
        sendToClient(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");

        // Last-Event-ID: 클라이언트가 마지막으로 수신한 데이터의 id값
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            // 받지 못한 데이터가 있다면 Last-Event-ID를 기준으로 그 뒤의 데이터를 추출해 알림 보냄
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
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

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long uid, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(uid);
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendToClient(emitter, entry.getKey(), emitterId, entry.getValue()));
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

    // 페이지형식으로 확인할때마다 한번에 불러오는게 나을듯
    public Page<NoticeResponse> getNotices(Long receiverID, int pageNo, int pageSize) {
        User receiver = userService.findById(receiverID);
        Pageable itemPage =  PageRequest.of(pageNo, pageSize);

        Page<Notice> noticePage = noticeRepository.findByReceiverID(receiver, itemPage);
        if (noticePage.isEmpty()){
            throw new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND);
        }

        return noticePage.map(notice -> findOne(notice.getId()));
    }

    public NoticeResponse getResponseObject(Notice notice) {
        return noticeMapper.toDto(notice);
    }
}
