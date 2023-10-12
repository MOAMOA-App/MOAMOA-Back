package org.zerock.moamoa.domain.DTO.email;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.Email;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    EmailMapper INSTANCE = Mappers.getMapper(EmailMapper.class);

    Email toEntity(EmailRequest emailRequest);

    EmailResponse toDto(Email email);
}
