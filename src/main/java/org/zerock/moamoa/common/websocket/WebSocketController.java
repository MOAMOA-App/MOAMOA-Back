package org.zerock.moamoa.common.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.zerock.moamoa.domain.DTO.chat.ChatMessageRequest;
import org.zerock.moamoa.service.ChatService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebSocketController {
    private final ChatService chatService;

    /**
     * send ChatMsg
     * MessageMapping 어노테이션을 통해 WebSocket으로 들어오는 메세지 발행 처리
     * 클라이언트에서 /app/chat/send 로 메시지 발행 -> 컨트롤러에서 @MessageMapping으로 받아줌
     * 받은 메시지를 데이터베이스에 저장하기 위해 chatService의 saveChat 메소드 호출
     * messagingTemplate의 convertAndSend 메소드를 통해 /topic/chat/{채팅방id}를 구독한 유저에게 해당 메시지를 보냄
     */
    @MessageMapping("/chat/send")
    public void message(@Payload ChatMessageRequest req) {
        chatService.saveChat(req);
    }
}

