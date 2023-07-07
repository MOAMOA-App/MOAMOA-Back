package org.zerock.moamoa.domain.DTO.wishlist;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.Instant;

@Data
public class WishListResponse {
    private Long id;
    private Product productId;
    private User userId;
    private Instant createdAt;

    @Builder
    public WishListResponse(Long id, Product productId, User userId, Instant createdAt) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
