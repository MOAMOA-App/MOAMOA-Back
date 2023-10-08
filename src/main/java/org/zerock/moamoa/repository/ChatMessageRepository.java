package org.zerock.moamoa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllChatByChatRoom(ChatRoom chatRoom);

    Page<ChatMessage> findAllChatByChatRoom(ChatRoom chatRoom, Pageable pageable);
}
