package org.zerock.moamoa.domain.DTO.wishlist;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.entity.WishList;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-21T17:48:27+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class WishListMapperImpl implements WishListMapper {

    @Override
    public WishList toEntity(WishListRequest wishListRequest) {
        if ( wishListRequest == null ) {
            return null;
        }

        WishList.WishListBuilder wishList = WishList.builder();

        wishList.product( wishListRequest.getProduct() );
        wishList.user( wishListRequest.getUser() );

        return wishList.build();
    }

    @Override
    public WishListResponse toDto(WishList wishList) {
        if ( wishList == null ) {
            return null;
        }

        WishListResponse.WishListResponseBuilder wishListResponse = WishListResponse.builder();

        wishListResponse.id( wishList.getId() );
        wishListResponse.createdAt( wishList.getCreatedAt() );

        return wishListResponse.build();
    }
}
