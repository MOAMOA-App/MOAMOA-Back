package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.domain.DTO.user.UserProfileUpdateRequest;
import org.zerock.moamoa.domain.DTO.user.UserPwChangeRequest;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.DTO.wishlist.WishListResponse;
import org.zerock.moamoa.service.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("myinfo")
public class MyInfoController {
    private final UserService userService;
    private final ProductService productService;
    private final PartyService partyService;
    private final WishListService wishListService;


    @GetMapping("profile")
    private UserProfileResponse getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return userService.getMyProfile(email);
    }

    /**
     * 프로필 수정
     */
    @PutMapping("profile")
    public UserResponse updateProfile(Authentication authentication,
                                      @ModelAttribute("profile") UserProfileUpdateRequest profileUpdateRequest) {
        return userService.updateProfile(profileUpdateRequest, authentication.getPrincipal().toString());
    }

    /**
     * 로그인한 상태에서 비밀번호 찾기
     */
    @PostMapping("password")
    public ResultResponse updatePW(Authentication authentication,
                                   @ModelAttribute("userpw") UserPwChangeRequest request){
        return userService.updatePwLogin(request, authentication.getPrincipal().toString());
    }

    /**
     * 내가 작성한 게시글 불러옴
     */
    @GetMapping("post")
    public Page<ProductResponse> getMyPosts(
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
    public Page<PartyResponse> getJoinParty(Authentication auth,
                                            @RequestParam(defaultValue = "0") int pageNo,
                                            @RequestParam(defaultValue = "8") int pageSize) {
        return partyService.findPageByBuyer(auth.getPrincipal().toString(), pageNo, pageSize);
    }

    /**
     * 내가 찜한 게시글 불러옴
     */
    @GetMapping("heart")
    public Page<WishListResponse> getUserWishList(Authentication auth,
                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                  @RequestParam(defaultValue = "8") int pageSize) {
        return wishListService.findPageByUser(auth.getPrincipal().toString(), pageNo, pageSize);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("")
    public ResultResponse deleteUser(Authentication authentication) {
        return userService.removeUser(authentication.getPrincipal().toString());
    }

}
