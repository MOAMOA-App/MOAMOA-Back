package org.zerock.moamoa.domain.DTO.notice;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.Notice;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticeMapper {
    NoticeMapper INSTANCE = Mappers.getMapper(NoticeMapper.class);

    Notice toEntity(NoticeSaveRequest noticeSaveRequest);

    NoticeResponse toDto(Notice notice);

    List<NoticeResponse> toDtoList(List<Notice> notices);
}
