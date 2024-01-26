package org.zerock.moamoa.domain.DTO.chat;

import lombok.Data;
import org.zerock.moamoa.domain.entity.ChatMessage;

@Data
public class ChatMessageResponse {
    private Long id;
    private Long chatRoomId;
    private String senderCode;
    private String message;
    private Boolean readOrNot;

    public static ChatMessageResponse toDto(ChatMessage chatMessage){
        ChatMessageResponse res = new ChatMessageResponse();
        res.chatRoomId = ChatRoomResponse.fromEntity(chatMessage.getChatRoom()).getId();
        res.message = chatMessage.getMessage();
        res.senderCode = chatMessage.getSender().getCode();
        res.readOrNot = chatMessage.getReadOrNot();
        return res;
    }
}
