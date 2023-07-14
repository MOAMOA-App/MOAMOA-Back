package org.zerock.moamoa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.service.PartyService;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;
import org.zerock.moamoa.service.WishListService;

@RestController
@RequestMapping("/myinfo")
public class MyinfoController { // 맨 긑에 붙은 id를 /profile
	private UserService userService;
	private WishListService wishListService;
	private ProductService productService;
	private PartyService partyService;

	/** 회원탈퇴 */
	@DeleteMapping("/{uid}")
	public Object deleteUser(@PathVariable Long uid) {
		userService.removeUser(uid);
		return ResponseEntity.ok(SuccessMessage.USER_DELETE);
	}

	/**
	 * 내가 작성한 게시글 불러옴
	 * @param userid
	 * @param orderBy
	 * @param sortOrder
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */

	//    @GetMapping("/{userid}/userpost")
	//    public Page<ProductResponse> getMyPosts(
	//            @PathVariable Long userid,
	//            @RequestParam(defaultValue = "createdAt") String orderBy,   // 정렬 기준: 생성일
	//            @RequestParam(defaultValue = "DESC") String sortOrder,      // 기본적으로 내림차순 정렬
	//            @RequestParam(defaultValue = "0") int pageNo,
	//            @RequestParam(defaultValue = "8") int pageSize
	//    ){
	//        return productService.toResPost(userid, orderBy, sortOrder, pageNo, pageSize);
	//    }

	// 참여자 목록 확인-> 냅다 만들엇는데 생각해보니까 그냥 PartyController 쪽에서 만든거 쓰면 되는거아닌지...
	//    @GetMapping("/{userid}/userpost/{pid}/joinlist")
	//    public List<PartyResponse> readByProductList(@PathVariable Long userid,
	//                                                 @RequestParam Long pid) {
	//        return partyService.getByProduct(pid);
	//    }

	//    /**
	//     * 내가 참여한 게시글 불러옴
	//     * @param id
	//     * @return
	//     */
	// YJ: 참여했을 때 이 리스트에 추가되어야 함: Party 생성되었을 때 buyer에 userid 있으면 생성
	// 페이지 형식으로 받아야-> 정렬 기준 날짜가 참여일이 되어야 함 (party의 createdat 사용!)
	// 거래 취소도 가능해야

	// 임시 주석처리
	//    @GetMapping("/myinfo/{id}/userparty")
	//    public ResponseEntity<List<UserDTO>> getMyUserParty(@PathVariable("id") Long id) {
	//        try {
	//            List<UserDTO> userDTOList = userService.getMyUserParty(id);
	//            return ResponseEntity.ok(userDTOList);
	//        } catch (Exception e) {
	//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	//        }
	//    }

	// 내가 참여한 리스트 불러옴
	//    @GetMapping("/{userid}/userpost/{pid}/userparty")
	//    public List<PartyResponse> readByProductList(@PathVariable Long userid,
	//                                                 @RequestParam Long pid) {
	//        return partyService.getByBuyer(userid);// 어; 어라이게아닌데
	//    }

	/** 내가 찜한 게시글 불러옴 */
	// YJ: ResponseEntity->ProductResponse 변경방법 알아보기
	@GetMapping("/{id}/userheart")
	public ResponseEntity<List<ProductResponse>> getUserWishList(@PathVariable("id") Long userId) {
		List<ProductResponse> products = wishListService.WishToProduct(userId);
		return ResponseEntity.ok(products);
	}

	/** 내가 찜한 게시글 삭제 */
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
