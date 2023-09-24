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
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.service.PartyService;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("myinfo")
public class MyInfoController { // 맨 긑에 붙은 id를 /profile
    private final UserService userService;
    private final ProductService productService;
    private final PartyService partyService;

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
     * 내가 작성한 게시글 불러옴
     */
    @GetMapping("post")
    public Page<ProductResponse> getMyPosts(
            @PathVariable Long userid,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "8") int pageSize
    ) {
        return productService.toResPost(userid, pageNo, pageSize);
    }

//    /**
//     * 내가 참여한 게시글 불러옴
//     */
//    // YJ: 참여했을 때 이 리스트에 추가되어야 함: Party 생성되었을 때 buyer에 userid 있으면 생성
//    // 페이지 형식으로 받아야-> 정렬 기준 날짜가 참여일이 되어야 함 (party의 createdat 사용!) <해결
//    // 거래 취소도 가능해야->
//    @GetMapping("post")
//    public Page<ProductResponse> getMyUserParty(Authentication authentication,
//                                                @RequestParam(defaultValue = "0") int pageNo,
//                                                @RequestParam(defaultValue = "8") int pageSize) {
//        return productService.toResParty(authentication.getPrincipal().toString(), pageNo, pageSize);
//    }

    // 내가 참여한 리스트 불러옴
    @GetMapping("party")
    public List<PartyResponse> getJoinParty(@PathVariable Long userid) {
        return partyService.getByBuyer(userid);
    }

    /**
     * 내가 찜한 게시글 불러옴
     */
    @GetMapping("heart")
    public Page<ProductResponse> getUserWishList(@PathVariable("id") Long userId,
                                                 @RequestParam(defaultValue = "0") int pageNo,
                                                 @RequestParam(defaultValue = "8") int pageSize) {
        return productService.toResWish(userId, pageNo, pageSize);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("")
    public ResultResponse deleteUser(Authentication authentication) {
        return userService.removeUser(authentication.getPrincipal().toString());
    }

}
