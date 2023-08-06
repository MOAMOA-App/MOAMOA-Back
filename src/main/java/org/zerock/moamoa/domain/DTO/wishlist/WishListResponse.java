package org.zerock.moamoa.domain.DTO.wishlist;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import java.time.Instant;

@Data
public class WishListResponse {
    private Long id;
    private ProductResponse productId;
    private UserProfileResponse userId;
    private Instant createdAt;

    @Builder
    public WishListResponse(Long id, ProductResponse productId, UserProfileResponse userId, Instant createdAt) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
