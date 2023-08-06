package org.zerock.moamoa.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(Long id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    public void deleteById(Long id) {
        emitters.remove(id);
    }

    public SseEmitter get(Long id) {
        return emitters.get(id);
    }
}