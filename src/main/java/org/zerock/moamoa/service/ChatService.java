package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.chat.ChatMessageRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomResponse;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ChatMessageRepository;
import org.zerock.moamoa.repository.ChatRoomRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final SimpMessagingTemplate template;   //특정 브로커로 메세지 전달


    public Page<ChatRoomResponse> getPageByProjectId(Long pid, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Product product = productRepository.findByIdOrThrow(pid);
        Long id = product.getId();
        Page<ChatRoom> rooms = chatRoomRepository.findAllByProductId(product, pageable);
        return rooms.map(ChatRoomResponse::fromEntity);
    }

    // 유저의 채팅방 불러옴
    public Page<ChatRoomResponse> getPageByUserId(String username, int pageNo, int pageSize) {
        User user = userRepository.findByEmailOrThrow(username);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        // 채팅방 seller랑 user 둘다로 찾음!
        Page<ChatRoom> rooms = chatRoomRepository.findAllBySellerIdAndUserId(user, user, pageable);
        return rooms.map(ChatRoomResponse::fromEntity);
    }

    public ChatRoom findByRoomId(Long id){
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ROOM_NOT_FOUND));
    }

    public List<ChatRoom> roomFindAll() {
        return chatRoomRepository.findAll();
    }

    public List<ChatMessage> messageFindAll() {
        return chatMessageRepository.findAll();
    }

    public Page<ChatMessage> roomMessageFindAll(ChatRoom chatRoom, Pageable pageable){
        return chatMessageRepository.findAllChatByChatRoom(chatRoom, pageable);
    }

    public Boolean isChatRoomExists(Product product, User seller, User user) {
        return chatRoomRepository.existsByProductIdAndSellerIdAndUserId(product, seller, user);
    }


    public ChatRoomResponse saveChatRoom(ChatRoomRequest request) {
        Product product = productRepository.findByIdOrThrow(request.getProductId());
        User seller = product.getUser();
        User buyer = userRepository.findByIdOrThrow(request.getUserId());
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.build(product, seller, buyer);
        chatRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomResponse.fromEntity(chatRoom);
    }

    public void saveChat(ChatMessageRequest req) {
        template.convertAndSend("/topic/chat/" + req.getChatRoom(), req);

        // 메시지 저장
//        User sender = userRepository.findByEmailOrThrow(name);
        User sender = userRepository.findByIdOrThrow(req.getSender());

        ChatRoom chatRoom = findByRoomId(req.getChatRoom());
        ChatMessage chatMessage = new ChatMessage(chatRoom, sender, req.getMessage(), false);
        chatMessageRepository.save(chatMessage);
    }

    public ChatRoomResponse findRoomById(Long id) {
        ChatRoom Room = findByRoomId(id);
        return ChatRoomResponse.fromEntity(Room);
    }
}
