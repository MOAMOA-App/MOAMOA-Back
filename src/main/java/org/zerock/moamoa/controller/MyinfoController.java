package org.zerock.moamoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.ProductDTO;
import org.zerock.moamoa.domain.DTO.WishListDTO;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MyinfoController {
    @Autowired
    private UserService userService;

    @Autowired
    private final ProductService productService;


    public MyinfoController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * 내가 작성한 게시글 불러옴
     * @param userId
     * @return
     */
    @GetMapping("/myinfo/userpost/{id}")
    public List<ProductDTO> getUserPosts(@PathVariable("id") Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id=" + userId));

        return productService.getProductsByUserId(userId);
    }

    /**
     * 내가 참여한 게시글 불러옴
     * @param id
     * @return
     */
    // -> 참여했을 때 이 리스트에 추가되어야 함

    // 임시 주석처리
//    @GetMapping("/myinfo/userparty/{id}")
//    public ResponseEntity<List<UserDTO>> getMyUserParty(@PathVariable("id") Long id) {
//        try {
//            List<UserDTO> userDTOList = userService.getMyUserParty(id);
//            return ResponseEntity.ok(userDTOList);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    /**
     * 내가 찜한 게시글 불러옴
     * @param userId
     * @return
     */
    @GetMapping("/myinfo/userheart/{id}")
    public ResponseEntity<?>
           // <List<WishListDTO>>   // 원래 이건데 메시지 반환하려니까 충돌나서 일단 이걸로 바꿔둠
                                    // 추후 데이터 추가해서 테스트 예정
        getUserWishLists(@PathVariable("id") Long userId) {

        // User의 위시리스트 목록 불러옴
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + userId));
        List<WishList> wishLists = user.getWishLists();

        // 위시리스트 목록을 WishListDTO로 변환
        List<WishListDTO> wishListDTOs = wishLists.stream()
                .map(WishListDTO::new)
                .collect(Collectors.toList());  // 변환된 WishListDTO 요소들을 리스트로 수집

        if (wishListDTOs.isEmpty()) {
            String message = "위시리스트가 존재하지 않습니다.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        // 변환된 위시리스트 DTO 목록을 ResponseEntity에 담아 응답, ok 상태코드(200)와 함께 DTO 목록 반환
        return ResponseEntity.ok(wishListDTOs);
    }

    @DeleteMapping("/myinfo/userheart/{id}")
    public ResponseEntity<Void> removeUserWishList(@PathVariable("id") Long userId,
                                                   @RequestParam("wishListId") Long wishListId) {
        // User의 위시리스트 목록 불러옴
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + userId));

        WishList wishList = user.getWishLists().stream()
                .filter(wl -> wl.getId().equals(wishListId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 위시리스트가 없습니다. id=" + wishListId));

        // User의 위시리스트 제거, 변경사항 저장
        user.removeWishList(wishList);
        userService.saveUser(user);

        return ResponseEntity.noContent().build(); // 성공적으로 삭제되었을 때는 204 No Content 상태코드 반환
    }

}
