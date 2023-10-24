package org.zerock.moamoa.domain.DTO.wishlist;

import lombok.Data;

@Data
public class WishListResponse {
    private Long productId;
    private boolean status;
    private String message;

    public WishListResponse(Long productId, boolean status, String message) {
        this.productId = productId;
        this.status = status;
        this.message = message;
    }
}
