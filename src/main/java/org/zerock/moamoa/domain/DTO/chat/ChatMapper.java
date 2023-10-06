package org.zerock.moamoa.domain.DTO.chat;

import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.ChatMessage;

public interface ChatMapper {
    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    ChatMessage toEntity(ChatMessageRequest chatMessageRequest);

    ChatMessageResponse toDto(ChatMessage chatMessage);
}
