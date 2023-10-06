package org.zerock.moamoa.controller;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.common.auth.JwtTokenProvider;
import org.zerock.moamoa.common.email.EmailRequest;
import org.zerock.moamoa.common.email.EmailMessage;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.common.message.OkResponse;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.joinEmails.*;
import org.zerock.moamoa.domain.DTO.user.*;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.user.*;
import org.zerock.moamoa.service.AuthService;
import org.zerock.moamoa.service.JoinEmailService;
import org.zerock.moamoa.service.UserService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final JoinEmailService joinEmailService;

    // 일단 만들어놓음 (나중에 백엔드에서 인증코드 비교하게 되면 사용)
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, LocalDateTime> verificationCodeExpiration = new HashMap<>(); // 유효 기간을 저장하는 맵
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest request) {
        return authService.login(request);
    }

    /**
     * oAuth 로그인
     */
    @GetMapping("/oauth")
    public Claims val(@RequestParam("accessToken") String accessToken,
                      @RequestParam("refreshToken") String refreshToken) {
        return jwtTokenProvider.parseClaims(accessToken);
    }

    /**
     * 토큰갱신 API
     */
    @GetMapping("/refresh")
    public UserRefreshResponse refreshToken(@RequestHeader("refresh_token") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public UserResponse signUp(@RequestBody UserSignupRequest userSignupRequest) throws Exception {
        // @RequestBody 어노테이션은 요청의 본문에 포함된 데이터를 AnnounceRequest 객체로 변환하여 announce 변수에 할당
        return userService.saveUser(userSignupRequest);
    }

    /**
     * 이메일 중복 확인
     */
    @PostMapping("/email/verify")
    public ResultResponse emailVerify(@RequestBody VerifyRequest verifyRequest) throws Exception {
        return userService.emailVerify(verifyRequest);
    }

    /**
     * 비밀번호 중복 확인
     */
    @PostMapping("/password/verify")
    public ResultResponse passwordVerify(@RequestBody UserLoginRequest verifyRequest) throws Exception {
        return userService.passwordVerify(verifyRequest);
    }

    /**
     * 이메일 인증번호 보냄
     */
    @PostMapping("/email/request")
    public CompletableFuture<JoinEmailtoClientResponse> sendVerifyEmail(@RequestBody EmailRequest emailReq)
            throws MessagingException, UnsupportedEncodingException {
        // User 엔티티에 이메일 있는지 이메일 중복 검사
        String email = emailReq.getEmail();
        if (userService.isEmailExist(email)) {
            throw new InvalidValueException(ErrorCode.INVALID_EMAIL_EXIST);
        }
        return joinEmailService.sendEmail(emailReq);
    }

    // updateauth 인증번호 확인
    // 이쪽에서 유저가 입력한 인증코드 받아서 이메일이랑 인증코드 같은지 확인하고 같을시 ㅇㅋ맞음, 다를시 ㄴㄴ틀림 보내줘야
    @PutMapping("/email/response")
    public JoinEmailtoClientResponse updateEmailAuth(@RequestBody JoinEmailAuthUpdateRequest authReq) {
        return joinEmailService.updateAuth(authReq);
    }

}
