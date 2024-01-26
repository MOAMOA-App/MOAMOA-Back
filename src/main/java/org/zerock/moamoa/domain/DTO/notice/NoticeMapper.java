package org.zerock.moamoa.domain.DTO.notice;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.Notice;

@Mapper(componentModel = "spring")
public interface NoticeMapper {
    NoticeMapper INSTANCE = Mappers.getMapper(NoticeMapper.class);

    Notice toEntity(NoticeSaveRequest noticeSaveRequest);

    @Mapping(source = "receiver.id", target = "receiver")
    NoticeResponse toDto(Notice notice);
}
