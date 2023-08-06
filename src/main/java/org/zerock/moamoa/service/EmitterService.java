package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zerock.moamoa.repository.EmitterRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmitterService {
    private final EmitterRepository emitterRepository;

    public void sendNotification(Long receiverId, String message) {
        SseEmitter emitter = emitterRepository.get(receiverId);
        if (emitter != null) {
            try {
                emitter.send(message);
            } catch (IOException e) {
                // 예외 처리 로직
            }
        }
    }
}
