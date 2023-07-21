package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.service.PartyService;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.domain.DTO.user.UserProfileResponse;
import org.zerock.moamoa.service.UserService;
import org.zerock.moamoa.service.WishListService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/myinfo")
public class MyinfoController { // 맨 긑에 붙은 id를 /profile
	private final UserService userService;
    private final WishListService wishListService;
    private final ProductService productService;
    private final PartyService partyService;

    @GetMapping("")
	private UserProfileResponse getMyProfile(Authentication auth) {
		String email = auth.getName();
		return userService.getMyProfile(email);
	}

    /** 내가 작성한 게시글 불러옴 */
    @GetMapping("/{userid}/userpost")
    public Page<ProductResponse> getMyPosts(
            @PathVariable Long userid,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "8") int pageSize
    ){
        return productService.toResPost(userid, pageNo, pageSize);
    }

	// 참여자 목록 확인-> 냅다 만들엇는데 생각해보니까 그냥 PartyController 쪽에서 만든거 쓰면 되는거아닌지...
	//    @GetMapping("/{userid}/userpost/{pid}/joinlist")
	//    public List<PartyResponse> readByProductList(@PathVariable Long userid,
	//                                                 @RequestParam Long pid) {
	//        return partyService.getByProduct(pid);
	//    }


    /** 내가 참여한 게시글 불러옴 */
    // YJ: 참여했을 때 이 리스트에 추가되어야 함: Party 생성되었을 때 buyer에 userid 있으면 생성
    // 페이지 형식으로 받아야-> 정렬 기준 날짜가 참여일이 되어야 함 (party의 createdat 사용!) <해결
    // 거래 취소도 가능해야->
    @GetMapping("/{id}/userparty")
    public Page<ProductResponse> getMyUserParty(@PathVariable("id") Long userId,
                                                @RequestParam(defaultValue = "0") int pageNo,
                                                @RequestParam(defaultValue = "8") int pageSize) {
        return productService.toResParty(userId, pageNo, pageSize);
    }

    // 내가 참여한 리스트 불러옴
    @GetMapping("/{userid}/userparty/{pid}/userparty")
    public List<PartyResponse> getJoinParty(@PathVariable Long userid,
                                            @RequestParam Long pid) {
        return partyService.getByBuyer(userid);
    }

    /** 내가 찜한 게시글 불러옴 */
    // YJ: ResponseEntity->ProductResponse 변경방법 알아보기
    @GetMapping("/{id}/userheart")
    public Page<ProductResponse> getUserWishList(@PathVariable("id") Long userId,
                                                 @RequestParam(defaultValue = "0") int pageNo,
                                                 @RequestParam(defaultValue = "8") int pageSize) {
        return productService.toResWish(userId, pageNo, pageSize);
    }

    /**
     * 내가 찜한 게시글 삭제
     *
     * @param uid
     * @param wishListId
     * @return
     */
    @DeleteMapping("/{uid}/userheart/{wishListId}")
    public ResponseEntity<String> removeUserWishList(@PathVariable("id") Long uid,
                                                     @RequestParam("wishListId") Long wishListId) {
        try {
            wishListService.removeWish(wishListId);
            return ResponseEntity.ok("Wish removed successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
