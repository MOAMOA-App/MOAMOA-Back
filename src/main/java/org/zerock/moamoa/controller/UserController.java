package org.zerock.moamoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.SignupRequest;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, EmailService emailService, ProductService productService, WishListService wishListService, UserRepository userRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    // 일단 걍 ResponseEntity로 작성했는데 나중에 리다이렉션되도록 바꿔야할지도... (RedirectView)
    // ResponseEntity: httpentity를 상속(HttpStatus, HttpHeaders HttpBody 포함,
    // 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스. 사용자의  HttpRequest에 대한 응답 데이터가 포함.
    // Client의 요청에 대한 응답을 한번 더 감싸는 역할.
    // 에러 코드와 같은 HTTP상태 코드를 전송하고 싶은 데이터와 함께 전송할 수 있기 때문에 좀 더 세밀한 제어가 필요한 경우 사용
    // ResponseEntity 사용시 주의점: void 처리할 때 Objects 로 데이터 타입을 명시하는 게 가독성, 에러, 유지보수 측면에 좋음

    /** 로그인(GetMapping)
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity<String> getLoginPage() {
        return ResponseEntity.ok("로그인 페이지");
    }

    /**
     * 로그인(PostMapping)
     * @param email
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        try {
            // 주어진 이메일로 데이터베이스에서 사용자 찾음
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // UserService의 메서드를 사용하여 비밀번호 확인
                boolean isPasswordCorrect = userService.checkPassword(password, user.getPassword());

                if (isPasswordCorrect) {
                    // 비밀번호가 일치하는 경우 로그인 성공
                    return ResponseEntity.ok("로그인 성공");
                } else {
                    // 비밀번호가 일치하지 않는 경우
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다");
                }
            } else {
                // 이메일이 존재하지 않는 경우
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 이메일입니다");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인에 실패했습니다");
        }
    }

    /**
     * 회원가입(GetMapping)
     * @return
     */
    @GetMapping("/signup")
    public ResponseEntity<String> getSignUpPage() {
        return ResponseEntity.ok("회원가입 페이지");
    }

    /**
     * 회원가입(PostMapping)
     * @param request
     * @return
     */
    // TODO: 비밀번호 두번 입력해서 둘이 같은지도 확인해야힘
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestParam() SignupRequest request) {
        try {
            String email = request.getEmail();
            String name = request.getName();
            String password = request.getPassword();

            // 이메일 인증 코드 보냄
            emailService.sendSimpleMessage(email);

            // 유저 등록: 생성된 User 객체를 UserService의 saveUser 메서드를 호출하여 저장
            userService.registerUser(email, name, password);

            return ResponseEntity.ok("회원가입 성공.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패");
        }
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
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.ok("User with ID " + id + " has been deleted.");
    }

}
