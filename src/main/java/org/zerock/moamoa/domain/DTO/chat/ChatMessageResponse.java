package org.zerock.moamoa.domain.DTO.chat;

import lombok.Data;
import org.zerock.moamoa.domain.entity.ChatMessage;
import org.zerock.moamoa.domain.entity.ChatRoom;

@Data
public class ChatMessageResponse {
    private Long id;
    private ChatRoom chatRoom;
    private Long sender;
    private String message;
    private Boolean readOrNot;

    public static ChatMessageResponse toDto(ChatMessage chatMessage){
        ChatMessageResponse res = new ChatMessageResponse();
        res.chatRoom = chatMessage.getChatRoom();
        res.message = chatMessage.getMessage();
        res.sender = chatMessage.getSender().getId();
        res.readOrNot = chatMessage.getReadOrNot();
        return res;
    }
}
