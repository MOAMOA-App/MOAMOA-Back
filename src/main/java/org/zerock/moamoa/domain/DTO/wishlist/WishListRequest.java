package org.zerock.moamoa.domain.DTO.wishlist;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

@Data
public class WishListRequest {
    private Long productId;
    private boolean status;
    private User user;
    private Product product;
}
