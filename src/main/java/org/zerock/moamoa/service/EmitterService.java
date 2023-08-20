package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zerock.moamoa.repository.EmitterRepositoryImpl;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmitterService {
    private final EmitterRepositoryImpl emitterRepositoryImpl;

//    public void sendNotice(Long receiverId, String message) {
//        SseEmitter emitter = emitterRepositoryImpl.findAllEmitterStartWithByMemberId(receiverId);
//        if (emitter != null) {
//            try {
//                emitter.send(message);
//            } catch (IOException e) {
//                // 예외 처리 로직
//            }
//        }
//    }
}
