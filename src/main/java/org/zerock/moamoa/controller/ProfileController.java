package org.zerock.moamoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.zerock.moamoa.domain.DTO.ProductDTO;
import org.zerock.moamoa.domain.DTO.ProfileDTO;
import org.zerock.moamoa.domain.DTO.UserDTO;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private final ProductService productService;

    public ProfileController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/")
    public RedirectView redirectToProfile() {
        Long id = 41L; // 리다이렉트할 사용자 ID를 지정
        String redirectUrl = "/profile/" + id; // 리다이렉트할 URL 생성
        return new RedirectView(redirectUrl);
    }

    /**
     * 프로필 조회 (UserDTO 사용해봄)
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            ProfileDTO profileDTO = new ProfileDTO(user);
            return ResponseEntity.ok(profileDTO); // 성공 상태 코드(200 OK)와 함께 사용자 정보를 클라이언트에게 반환
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * 프로필 수정
     * @param id
     * @param updatedUser
     * @return
     */
    @PutMapping("/{id}/edit") // api명세 참고
    public ResponseEntity<User> updateProfile(@PathVariable Long id, @RequestBody User updatedUser) {
        // @PathVariable을 사용하여 URL에서 id를 추출
        // @RequestBody를 사용하여 요청 본문의 JSON을 User 객체로 매핑

        // userService.findById()로 해당 id의 사용자 찾음, 수정할 필드들 업데이트
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setNick(updatedUser.getNick());
            user.setProfImg(updatedUser.getProfImg());
            user.setPassword(updatedUser.getPassword());
            user.setAddress(updatedUser.getAddress());
            user.setDetailAddress(updatedUser.getDetailAddress());

            // userService.saveUser()를 호출하여 수정된 사용자 정보를 저장
            User updatedUserProfile = userService.saveUser(user);

            // 수정된 프로필 정보를 클라이언트에게 반환하기 위해 ResponseEntity.ok() 사용
            return ResponseEntity.ok(updatedUserProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
