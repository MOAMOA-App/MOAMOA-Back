package org.zerock.moamoa.common.notice;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository{
    // 동시성을 고려해 ConcurrentHashMap으로 구현, 이를 저장하거나 꺼내는 방식
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        // 새로운 Emitter 추가
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        // 수신한 이벤트를 캐시에 저장
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String userCode) {
        // 어떤 회원이 접속한 모든 Emitter 찾음
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userCode))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String userCode) {
        // 어떤 회원에게 수신된 이벤트를 캐시에서 모두 찾음
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userCode))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String id) {
        // ID 통해 Repository에서 Emitter  제거
        emitters.remove(id);
    }

    // Emitter와 이벤트를 찾을 때 startsWith을 사용하는 이유
    // : 저장할 때 뒤에 구분자로 회원의 ID를 사용하기 때문에 해당 회원과 관련된 Emitter와 이벤트들을 찾아옴

    @Override
    public void deleteAllEmitterStartWithId(Long uid) {
        // 저장된 모든 Emitter 제거
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(String.valueOf(uid))) {
                        emitters.remove(key);
                    }
                }
        );
    }

    @Override
    public void deleteAllEventCacheStartWithId(Long uid) {
        // 캐시에서 수신한 모든 이벤트 제거
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(String.valueOf(uid))) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}