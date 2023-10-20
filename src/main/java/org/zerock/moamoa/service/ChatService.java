package org.zerock.moamoa.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.chat.ChatMessageRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatMessageResponse;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;
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

    private final ApplicationEventPublisher eventPublisher;
    private final SimpMessagingTemplate template;   //특정 브로커로 메세지 전달

    public Page<ChatRoomResponse> getPageByProjectId(Long pid, String username, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Product product = productRepository.findByIdOrThrow(pid);

        User user = userRepository.findByEmailOrThrow(username);
        if (product.getUser() != user){
            throw new AuthException(ErrorCode.AUTH_NOT_FOUND);
        }

        Page<ChatRoom> rooms = chatRoomRepository.findAllByProductId(product, pageable);
        return rooms.map(room -> {
            ChatRoomResponse response = ChatRoomResponse.fromEntity(room);
            response.setMessages(findAllRoomMsg(room));
            return response;
        });
    }

    // 유저의 채팅방 불러옴
    public Page<ChatRoomResponse> getPageByUserId(String username, int pageNo, int pageSize) {
        User user = userRepository.findByEmailOrThrow(username);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        // 채팅방 seller랑 user 둘다로 찾음!
        Page<ChatRoom> rooms = chatRoomRepository.findAllBySellerIdAndUserId(user, user, pageable);

        return rooms.map(room -> {
            ChatRoomResponse response = ChatRoomResponse.fromEntity(room);
            response.setMessages(findAllRoomMsg(room));
            return response;
        });
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

    public List<ChatMessageResponse> findAllRoomMsg(ChatRoom chatRoom){
        return chatMessageRepository.findAllChatByChatRoom(chatRoom).stream()
                .map(ChatMessageResponse::toDto)
                .toList();
    }

    public Page<ChatMessage> findAllRoomMsg(ChatRoom chatRoom, Pageable pageable){
        return chatMessageRepository.findAllChatByChatRoom(chatRoom, pageable);
    }

    @Transactional
    public void saveChatRoom(ChatRoomRequest request) {
        Product product = productRepository.findByIdOrThrow(request.getProductId());
        User seller = product.getUser();
        User buyer = userRepository.findByIdOrThrow(request.getUserId());
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.build(product, seller, buyer);

        chatRoomRepository.save(chatRoom);
    }

    public void saveChat(ChatMessageRequest req) {
        template.convertAndSend("/topic/chat/" + req.getId(), req);
        User sender = userRepository.findByIdOrThrow(req.getSender());

        ChatRoom chatRoom = findByRoomId(req.getId());

        ChatMessage chatMessage = new ChatMessage(chatRoom, sender, req.getMessage(), false);
        chatMessageRepository.save(chatMessage);
    }

    public ChatRoomResponse findRoomById(Long id) {
        ChatRoom room = findByRoomId(id);
        ChatRoomResponse res = ChatRoomResponse.fromEntity(room);
        res.setMessages(findAllRoomMsg(room));
        return res;
    }
}
