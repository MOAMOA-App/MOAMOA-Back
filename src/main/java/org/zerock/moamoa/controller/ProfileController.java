package org.zerock.moamoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.zerock.moamoa.domain.DTO.user.UserProfileUpdateRequest;
import org.zerock.moamoa.domain.DTO.user.UserPwUpdateRequest;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private UserService userService;
    private final ProductService productService;

    public ProfileController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * 본인 프로필로 리다이렉트
     * @param id 접속한 유저의 id
     * @return RedirectView(redirectUrl) /profile -> /profile/{id}
     */
    @GetMapping("/")
    public RedirectView redirectToProfile(@PathVariable Long id) {
        String redirectUrl = "/profile/" + id;
        return new RedirectView(redirectUrl);
    }

    /**
     * 프로필 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getProfile(
            @PathVariable Long id
//            @RequestParam(required = false) String nick,
//            @RequestParam(required = false) String profImg,
//            @RequestParam(required = false) String email,
//            @RequestParam(required = false) String address,
//            @RequestParam(required = false) String detailAddress
    ) {
        UserResponse userResponse = userService.findOne(id);
        return ResponseEntity.ok(userResponse);
    }


    /**
     * 프로필 수정
     * @param id
     * @param UP
     * @return
     */
    @PutMapping("/{id}/edit")
    public UserResponse updateProfile(@PathVariable Long id,
                                      @ModelAttribute("profile") UserProfileUpdateRequest UP) {
        return userService.updateProfile(UP);
    }

    /**
     * 프로필에서 비밀번호 수정
     * @param id
     * @param UPw
     * @return
     * @throws Exception
     */
    @PutMapping("/{id}/editpw")
    public UserResponse updateUserPw(@PathVariable Long id,
                                     @ModelAttribute("UserPw") UserPwUpdateRequest UPw) throws Exception {
        return userService.updatePw(UPw);
    }
}
