package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.ChatRoom;
import org.zerock.moamoa.repository.ChatMessageRepository;
import org.zerock.moamoa.repository.ChatRoomRepository;
import org.zerock.moamoa.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class ChatRoomService {
    private final ChatMessageRepository chatMessageRepository;

    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatMessageRepository chatMessageRepository, UserRepository userRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }


    @Transactional
    public ChatRoom saveChatRoom(ChatRoom chatRoom){
        return chatRoomRepository.save(chatRoom);

    }




}
