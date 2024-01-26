package org.zerock.moamoa.common.notice;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String emitterId, Object event);
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String userCode);
    Map<String, Object> findAllEventCacheStartWithByMemberId(String userCode);
    void deleteById(String id);
    void deleteAllEmitterStartWithId(Long uid);
    void deleteAllEventCacheStartWithId(Long uid);
}