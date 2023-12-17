package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.product.ProductListResponse;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileUpdateRequest;
import org.zerock.moamoa.domain.DTO.user.UserPwChangeRequest;
import org.zerock.moamoa.service.PartyService;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;
import org.zerock.moamoa.service.WishListService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("myinfo")
public class MyInfoController {
    private final UserService userService;
    private final ProductService productService;
    private final PartyService partyService;
    private final WishListService wishListService;


    /**
     * 본인 프로필 불러오기
     */
    @GetMapping("profile")
    private UserResponse getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return userService.getMyProfile(email);
    }

    /**
     * 프로필 수정
     */
    @PutMapping("profile")
    public ResultResponse updateProfile(Authentication authentication,
                                        @RequestBody UserProfileUpdateRequest profileUpdateRequest) {
        return userService.updateProfile(profileUpdateRequest, authentication.getPrincipal().toString());
    }

    /**
     * 로그인한 상태에서 비밀번호 변경
     */
    @PostMapping("password")
    public ResultResponse updatePW(Authentication authentication,
                                   @RequestBody UserPwChangeRequest request) {
        return userService.updatePwLogin(request, authentication.getPrincipal().toString());
    }

    /**
     * 내가 작성한 게시글 불러옴
     */
    @GetMapping("post")
    public Page<ProductListResponse> getMyPosts(
            Authentication auth,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "8") int pageSize
    ) {
        return productService.findPageByUser(auth.getPrincipal().toString(), pageNo, pageSize);
    }

    /**
     * 내가 참여한 리스트 불러옴
     */
    @GetMapping("party")
    public Page<ProductListResponse> getJoinParty(Authentication auth,
                                            @RequestParam(defaultValue = "0") int pageNo,
                                            @RequestParam(defaultValue = "8") int pageSize) {
        return partyService.findPageByBuyer(auth.getPrincipal().toString(), pageNo, pageSize);
    }

    /**
     * 내가 찜한 게시글 불러옴
     */
    @GetMapping("heart")
    public Page<ProductListResponse> getUserWishList(Authentication auth,
                                                     @RequestParam(defaultValue = "0") int pageNo,
                                                     @RequestParam(defaultValue = "8") int pageSize) {
        return wishListService.findPageByUser(auth.getPrincipal().toString(), pageNo, pageSize);
    }

    /** 회원탈퇴 */
    @DeleteMapping("")
    public ResultResponse deleteUser(Authentication authentication) {
        return userService.removeUser(authentication.getPrincipal().toString());
    }

}
