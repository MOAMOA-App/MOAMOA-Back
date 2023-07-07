package org.zerock.moamoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.common.email.SignupRequest;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.user.UserLoginRequest;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.DTO.user.UserSignupRequest;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private EmailService emailService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, EmailService emailService, ProductService productService, WishListService wishListService, UserRepository userRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    /** 로그인 페이지 */
    @GetMapping("/login")
    public ResponseEntity<String> getLoginPage() {
        return ResponseEntity.ok("로그인 페이지");
    }

    /**
     * 로그인
     * @param userLoginRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public UserResponse login(@RequestBody UserLoginRequest userLoginRequest) throws Exception {
        return userService.login(userLoginRequest);
    }

    /**
     * 회원가입 페이지
     * @return
     */
    @GetMapping("/signup")
    public ResponseEntity<String> getSignUpPage() {
        return ResponseEntity.ok("회원가입 페이지");
    }

    /**
     * 회원가입
     * @param US
     * @return
     * @throws Exception
     */
    @PostMapping("/signup")
    public UserResponse signUp(@RequestBody UserSignupRequest US) throws Exception {
        // @RequestBody 어노테이션은 요청의 본문에 포함된 데이터를 AnnounceRequest 객체로 변환하여 announce 변수에 할당
        return userService.saveUser(US);
    }

    /**
     * 이메일 인증번호 보냄
     * @param email
     * @return
     * @throws Exception
     */
    @PostMapping("/signup/sendemail")
    public String emailConfirm(@RequestParam String email) throws Exception {
        return emailService.sendSimpleMessage(email);
    }

    /**
     * 이메일 인증코드 성공/실패 여부
     * @param code
     * @return
     */
    @PostMapping("/signup/emailverify")
    public ResponseEntity<String> verifyCode(@RequestParam String code) {
        if (isValidCode(code)) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 인증 코드");
        }
    }
    private boolean isValidCode(String code) {
        // 입력한 code가 EmailAuthCode와 같은지 확인
        return EmailServiceImpl.EmailAuthCode.equals(code);
    }

    /**
     * 회원탈퇴
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Object deleteUser(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.ok(SuccessMessage.USER_DELETE);
    }

}
