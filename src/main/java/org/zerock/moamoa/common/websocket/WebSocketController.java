package org.zerock.moamoa.common.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.zerock.moamoa.domain.DTO.chat.ChatMessageRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatMessageResponse;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.repository.ChatRoomRepository;
import org.zerock.moamoa.service.ChatService;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebSocketController {
    // STOMP over WebSocket
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;

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


//    // 채팅방 들어가면 원래 있었던 메시지 msg로 다 보내주는식인가봄 (전에 보냈던거 다시)
//    @MessageMapping(value = "/chat/enter")
//    public void enter(ChatMessage chatMessage){
//
//    }

    // Client 가 SEND 할 수 있는 경로
    // 참여자 목록에서 채팅하기 보낼 때
    // 아니면 걍 이걸 불러오는걸로하고 보내는건 아예 딴걸로
    // MessageMapping: 이 경로로 메시지 보냄
    @MessageMapping("/chatroom/{rid}/enter")    // 실제론 메세지 매핑으로 /chat/chatroom/{id}/enter 임
//    @SendTo("/queue/chats")    // WebSocketStompConfig에서 설정했던 곳으로
    public void sendMessage(@DestinationVariable("rid") Long rid,
                            @Payload ChatMessageRequest req) {
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(req.getChatRoom());
        // 입장시 메시지 보내주는 부분
        List < ChatMessage > chatList = chatService.roomMessageFindAll(chatRoom);
        if (chatList != null) {
            for (ChatMessage c : chatList) {
                req.setSender(c.getSender().getId());
                req.setMessage(c.getMessage());
            }
        }
        chatService.saveAndSendChat(req);
    }

    // 방에 들어온 상태에서 메시지 보낼 때
    // 처음 채팅방에 들어와서(참여자 목록에서 채팅하기 누를때 화면?) 메시지 보낼 때 채팅방 생성, 이후 메시지만 보냄
    // 이후 채팅하기나 채팅방 목록으로 들어가면 메시지만 보낼 수 있도록 하면 될 듯
    @MessageMapping("/chatroom/{rid}")
    public void message(@DestinationVariable("rid") Long rid, ChatMessageRequest req) {
        chatService.saveAndSendChat(req);
    }
}

//    @MessageMapping 을 통해 WebSocket 으로 들어오는 메세지 발행을 처리한다.
//    Client 에서는 prefix 를 붙여 "/pub/chat/enter"로 발행 요청을 하면
//    Controller 가 해당 메세지를 받아 처리하는데,
//    메세지가 발행되면 "/sub/chat/room/[roomId]"로 메세지가 전송되는 것을 볼 수 있다.
//    Client 에서는 해당 주소를 SUBSCRIBE 하고 있다가 메세지가 전달되면 화면에 출력한다.
//    이때 /sub/chat/room/[roomId]는 채팅방을 구분하는 값이다.
//    기존의 핸들러 ChatHandler 의 역할을 대신 해주므로 핸들러는 없어도 된다.