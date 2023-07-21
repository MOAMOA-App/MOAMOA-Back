package org.zerock.moamoa.domain.DTO.notice;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticeMapper {
    NoticeMapper INSTANCE = Mappers.getMapper(NoticeMapper.class);

    @Mapping(target = "senderID", source = "noticeSaveRequest.senderID")
    @Mapping(target = "receiverID", source = "noticeSaveRequest.receiverID")
    @Mapping(target = "referenceID", source = "noticeSaveRequest.referenceID")
    Notice toEntity(NoticeSaveRequest noticeSaveRequest);

    NoticeResponse toDto(Notice notice);

    List<NoticeResponse> toDtoList(List<Notice> notices);

    // 임시해결책
    default User mapToUser(Long value) {
        return new User(value);
    }
    default Product mapToProduct(Long value) {
        return new Product(value);
    }
}
