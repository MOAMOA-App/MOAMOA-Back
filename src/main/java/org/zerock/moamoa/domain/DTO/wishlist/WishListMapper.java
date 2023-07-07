package org.zerock.moamoa.domain.DTO.wishlist;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.zerock.moamoa.domain.entity.WishList;

@Mapper(componentModel = "spring")
public interface WishListMapper {
    WishListMapper INSTANCE = Mappers.getMapper(WishListMapper.class);

    WishList toEntity(WishListRequest wishListRequest);

    WishListResponse toDto(WishList wishList);
}
