package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomResponse;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ChatMessageRepository;
import org.zerock.moamoa.repository.ChatRoomRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    public Page<ChatRoomResponse> getPageByProjectId(Long pid, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Product product = productService.findById(pid);
        Long id = product.getId();
        Page<ChatRoom> rooms = chatRoomRepository.findAllByProductId(product, pageable);
        return rooms.map(ChatRoomResponse::fromEntity);
    }

    public List<ChatRoom> roomFindAll() {
        return chatRoomRepository.findAll();
    }

    public List<ChatMessage> messageFindAll() {
        return chatMessageRepository.findAll();
    }

    public ChatRoomResponse saveChatRoom(ChatRoomRequest request) {
        Product product = productService.findById(request.getProductId());
        User seller = product.getUser();
        User buyer = userRepository.findByIdOrThrow(request.getUserId());
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.build(product, seller, buyer);
        chatRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomResponse.fromEntity(chatRoom);
    }

    public ChatMessage saveChatMessage(String message, Long chatRoomId, Long senderId) {
        User sender = userRepository.findByIdOrThrow(senderId);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("chatRoom not found"));

        ChatMessage chatMessage = new ChatMessage();
        // chatMessage.setMessage(message);
        // chatMessage.setSender(sender);
        // chatMessage.setChatRoom(chatRoom);
        return chatMessageRepository.save(chatMessage);
    }


    public ChatRoomResponse findRoomById(Long id) {
        ChatRoom Room = chatRoomRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ROOM_NOT_FOUND));
        return ChatRoomResponse.fromEntity(Room);
    }

}
