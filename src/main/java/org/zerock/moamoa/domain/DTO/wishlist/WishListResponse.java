package org.zerock.moamoa.domain.DTO.wishlist;

import lombok.Data;

@Data
public class WishListResponse {
    private Long pid;
    private boolean status;
    private String message;

    public WishListResponse(Long pid, boolean status, String message) {
        this.pid = pid;
        this.status = status;
        this.message = message;
    }
}
