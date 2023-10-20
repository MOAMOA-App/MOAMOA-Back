package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.common.message.OkResponse;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.wishlist.WishListRequest;
import org.zerock.moamoa.domain.DTO.wishlist.WishListResponse;
import org.zerock.moamoa.service.WishListService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/heart")
public class WishListController {
    private final WishListService wishListService;

    @PostMapping("/{pid}")
    public ResultResponse saveWish(Authentication auth, @PathVariable Long pid
//                                     @RequestBody WishListRequest wishListRequest
    ){
        return wishListService.saveWish(auth.getPrincipal().toString(), pid);
    }

    @DeleteMapping("/{pid}")
    public ResultResponse removeWish(Authentication auth, @PathVariable Long pid) {
        return wishListService.removeWish(auth.getPrincipal().toString(), pid);
    }

}
