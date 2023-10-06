package org.zerock.moamoa.domain.DTO.chat;

import lombok.Data;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;

@Data
public class ChatMessageRequest {
    private Long ChatRoom;
    private Long sender;
    private String message;
}
