package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;

import java.time.LocalDateTime;

@Data
public class WishListDTO {
    private Long id;

    private Product productId;

    private User userId;

    private LocalDateTime createdAt;

    public WishListDTO(WishList wishList) {
        id = wishList.getId();
        productId = wishList.getProductId();
        userId = wishList.getUserId();
        createdAt = wishList.getCreatedAt();
    }
}
