package org.zerock.moamoa.service;

import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ChatMessageRepository;
import org.zerock.moamoa.repository.ChatRoomRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;

    public ChatService(ChatMessageRepository chatMessageRepository, ProductRepository productRepository, UserRepository userRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public List<ChatRoom> roomFindAll(){
        return chatRoomRepository.findAll();
    }

    @Transactional
    public List<ChatMessage> messageFindAll(){
        return chatMessageRepository.findAll();
    }

    @Transactional
    public ChatRoom saveChatRoom(Long sellerId, Long userId, Long Prodect_id){
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        User buyer = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(Prodect_id).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setSellerId(seller);
        chatRoom.setUserId(buyer);
        chatRoom.setProductId(product);
        return chatRoomRepository.save(chatRoom);
    }
    @Transactional
    public ChatMessage saveChatMessage(String message, Long chatRoomId, Long senderId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("chatRoom not found"));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(message);
        chatMessage.setSender(sender);
        chatMessage.setChatRoom(chatRoom);
        return chatMessageRepository.save(chatMessage);
    }


}
