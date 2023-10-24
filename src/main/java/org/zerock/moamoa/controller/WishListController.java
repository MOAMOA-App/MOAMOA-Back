package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.moamoa.domain.DTO.wishlist.WishListRequest;
import org.zerock.moamoa.domain.DTO.wishlist.WishListResponse;
import org.zerock.moamoa.service.WishListService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/heart")
public class WishListController {
    private final WishListService wishListService;

    /**
     * 위시리스트 변경
     */
    @PostMapping
    public WishListResponse setHeart(@RequestBody WishListRequest request, Authentication authentication) {
        return wishListService.changeWishList(request, authentication.getPrincipal().toString());
    }

}
