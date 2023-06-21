package org.zerock.moamoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.zerock.moamoa.domain.DTO.ProductDTO;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<Long> getUserHeartProducts(@PathVariable("id") Long userId) {
        // 위시리스트에 있는 상품들 불러옴, 가져온 상품 목록을 ProductDTO 객체로 변환하여 products 리스트에 저장
        List<ProductDTO> products = productService.getProductsByWishListUserId(userId);

        // products에서 상품id 추출-> map(ProductDTO::getId) 통해 ID 목록 생성
        // 생성된 ID 목록을 Collectors.toList() 사용해 productIds 리스트에 저장
        List<Long> productIds = products.stream()
                .map(ProductDTO::getId)
                .collect(Collectors.toList());

        // 사용자의 위시리스트에 있는 상품의 ID 목록을 클라이언트에게 보냄
        return productIds;
    }
}
