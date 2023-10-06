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

    public static ChatMessageResponse fromEntity(ChatMessage chatMessage){
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(chatMessage.getId());
        response.setChatRoom(chatMessage.getChatRoom());
        response.setSender(chatMessage.getSender().getId());
        response.setMessage(chatMessage.getMessage());
        response.setReadOrNot(chatMessage.getReadOrNot());
        return response;
    }
}
