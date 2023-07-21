package org.zerock.moamoa.common.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.service.ChatService;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebSocketController {
    // STOMP over WebSocket
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void sendChat(
            @Payload ChatMessage chatMessage,
            // STOMP over WebSocket은 Header를 포함할 수 있다
            @Headers Map<String, Object> headers,
            @Header("nativeHeaders") Map<String, String> nativeHeaders
    ) {
        log.info(chatMessage.toString());
        log.info(headers.toString());
        log.info(nativeHeaders.toString());
//        String time = new SimpleDateFormat("HH:mm").format(new Date());
//        chatMessage.setTime(time);
//        chatService.saveChatMessage(chatMessage);
//        simpMessagingTemplate.convertAndSend(
//                String.format("/topic/%s", chatMessage.getRoomId()),
//                chatMessage
//        );
    }
}
