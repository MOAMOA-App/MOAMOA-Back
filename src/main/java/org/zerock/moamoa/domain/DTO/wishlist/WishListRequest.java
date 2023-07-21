package org.zerock.moamoa.domain.DTO.wishlist;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

@Data
public class WishListRequest {
    private Product product;
    private User user;

    @Builder
    public WishListRequest(Product product, User user) {
        this.product = product;
        this.user = user;
    }
}
