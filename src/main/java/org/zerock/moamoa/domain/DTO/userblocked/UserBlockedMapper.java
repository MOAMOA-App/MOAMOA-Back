package org.zerock.moamoa.domain.DTO.userblocked;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.UserBlocked;

@Mapper(componentModel = "spring")
public interface UserBlockedMapper {
    UserBlockedMapper INSTANCE = Mappers.getMapper(UserBlockedMapper.class);

//    UserBlocked toEntity(UserBlockedRequest userBlockedRequest);

    UserBlockedResponse toDto(UserBlocked userBlocked);
}
