package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zerock.moamoa.repository.EmitterRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmitterService {
    private final UserRepository userRepository;
    private final EmitterRepository emitterRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;    // SSE 연결은 1시간동안 지속됨

    /**
     * SSE 연결
     * SseEmitter 생성/저장 후 필요할 때마다 해당 구독자가 생성한 SseEmitter를 불러와 이벤트의 응답 전송
     */
    public SseEmitter subscribe(String username, String lastEventId) {
        Long memberId = userRepository.findByEmailOrThrow(username).getId();

        // 누가 사용하고 있는 SSE emitter인지 구분
        // 시간을 emitterId에 붙여두면 데이터가 유실된 시점을 알 수 있음
        String emitterId = makeTimeIncludeId(memberId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // 시간이 만료된 경우 자동으로 레포지토리에서 삭제해줄 수 있는 콜백을 등록
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // memberID를 다시 시간으로 구분해줌
        // : 해당 회원 ID로 요청한 이벤트들 중 가장 마지막에 전송받은 데이터 구분
        // 근데 이거 걍 emitterId랑 똑같은거아닌가 왜 또만들어줬지...
        String eventId = makeTimeIncludeId(memberId);

        // 등록한 뒤 SseEmitter의 유효 시간동안 데이터가 전송되지 않으면 503 에러 발생
        // 이를 방지하기 위해 최초 연결 시 더미 이벤트 전송
        sendToClient(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");

        // Last-Event-ID: 클라이언트가 마지막으로 수신한 데이터의 id값
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            // 받지 못한 데이터가 있다면 Last-Event-ID를 기준으로 그 뒤의 데이터를 추출해 알림 보냄
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return emitter;
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

    private String makeTimeIncludeId(Long memberId) {
        return memberId + "_" + System.currentTimeMillis();
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
}
