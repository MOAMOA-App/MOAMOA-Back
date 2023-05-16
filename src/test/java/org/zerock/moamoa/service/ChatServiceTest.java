package org.zerock.moamoa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatServiceTest {
    @Autowired
    ChatService chatService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    @Test
    void saveChatMessage() {
    }

    @Test
    void roomFindAll() {
        List<ChatRoom> chatRoomList = chatService.roomFindAll();
    }

    @Test
    void messageFindAll() {
        List<ChatMessage> chatRoomList = chatService.messageFindAll();
    }

    @Test
    void saveChatRoom() {
        ChatRoom chatRoom = chatService.saveChatRoom(31L, 32L, 11L);

    }

    @Test
    void testSaveChatMessage() {
    }
}