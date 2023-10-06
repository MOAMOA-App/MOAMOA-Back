package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.common.message.OkResponse;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.wishlist.WishListRequest;
import org.zerock.moamoa.domain.DTO.wishlist.WishListResponse;
import org.zerock.moamoa.service.WishListService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class WishListController {
    private final WishListService wishListService;

    @PostMapping("/{pid}")
    public WishListResponse saveWish(@PathVariable Long pid,
                                     @RequestParam WishListRequest wishListRequest){
        return wishListService.saveWish(wishListRequest);
    }

    @DeleteMapping("/{pid}/wish/{wid}")
    public Object removeWish(@PathVariable Long pid,
                             @RequestParam Long wid) {
        wishListService.removeWish(wid);
        return new OkResponse(SuccessMessage.WISH_DELETE);
    }

}
