package org.zerock.moamoa.domain.DTO.chat;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private Long id;
    private Long sender;
    private String message;
}
