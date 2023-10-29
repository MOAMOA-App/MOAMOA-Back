package org.zerock.moamoa.common.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.moamoa.domain.DTO.chat.ChatMessageRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatMessageResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.repository.ChatMessageRepository;
import org.zerock.moamoa.repository.ChatRoomRepository;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.ChatService;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebSocketController {
    // STOMP over WebSocket
    private final SimpMessagingTemplate template;   //특정 브로커로 메세지 전달
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;


    @MessageMapping("")
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

    // 클라이언트에서 /app/chat/send 로 메시지를 발행 -> 컨트롤러에서 @MessageMapping으로 받아줌
    // 받은 메시지를 데이터베이스에 저장하기 위해 chatService의 saveChat 메소드 호출
    // messagingTemplate의 convertAndSend 메소드를 통해 /queue/채팅방id를 구독한 유저에게 해당 메시지를 보냄
    @MessageMapping("/chat/send")
    public void message(@Payload ChatMessageRequest req) {
        chatService.saveChat(req);
    }

//    // 채팅방id로 채팅 이력 불러옴
//    @GetMapping("/chat/find/{rid}")
//    public ResponseEntity<List<ChatMessageResponse>> getChats(
//            @PathVariable Long rid, Authentication auth,
//            @RequestParam(value = "no", defaultValue = "0") int pageNo,
//            @RequestParam(value = "size", defaultValue = "20") int pageSize) {
//
//        userRepository.findByEmailOrThrow(auth.getPrincipal().toString());
//        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(rid);
//
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//
//        Page<ChatMessageResponse> chatPage = chatMessageRepository.findAllChatByChatRoom(chatRoom, pageable)
//                .map(ChatMessageResponse::toDto);
//
//        List<ChatMessageResponse> chatList= chatMessageRepository.findAllChatByChatRoom(chatRoom)
//                .stream()
//                .map(ChatMessageResponse::toDto)
//                .toList();
//
//        return ResponseEntity.status(HttpStatus.OK).body(chatList);
//    }
}

//    @MessageMapping 을 통해 WebSocket 으로 들어오는 메세지 발행을 처리한다.
//    Client 에서는 prefix 를 붙여 "/pub/chat/enter"로 발행 요청을 하면
//    Controller 가 해당 메세지를 받아 처리하는데,
//    메세지가 발행되면 "/sub/chat/room/[roomId]"로 메세지가 전송되는 것을 볼 수 있다.
//    Client 에서는 해당 주소를 SUBSCRIBE 하고 있다가 메세지가 전달되면 화면에 출력한다.
//    이때 /sub/chat/room/[roomId]는 채팅방을 구분하는 값이다.
//    기존의 핸들러 ChatHandler 의 역할을 대신 해주므로 핸들러는 없어도 된다.