package org.zerock.moamoa.domain.DTO.joinEmails;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.JoinEmail;

@Mapper(componentModel = "spring")
public interface JoinEmailMapper {
    JoinEmailMapper INSTANCE = Mappers.getMapper(JoinEmailMapper.class);

    JoinEmail toEntity(JoinEmailRequest joinEmailRequest);

    JoinEmailResponse toDto(JoinEmail joinEmail);
}
