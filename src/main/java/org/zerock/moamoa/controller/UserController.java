package org.zerock.moamoa.controller;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.common.auth.JwtTokenProvider;
import org.zerock.moamoa.domain.DTO.email.EmailAddrRequest;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.domain.DTO.email.*;
import org.zerock.moamoa.domain.DTO.user.*;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.service.AuthService;
import org.zerock.moamoa.service.EmailService;
import org.zerock.moamoa.service.UserService;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

import static org.zerock.moamoa.domain.enums.EmailType.EMAIL_JOIN;
import static org.zerock.moamoa.domain.enums.EmailType.EMAIL_PW;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final EmailService emailService;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserCheckRequest request) {
        return authService.login(request);
    }

    /**
     * oAuth 로그인
     */
    @GetMapping("/oauth")
    public Claims val(@RequestParam("accessToken") String accessToken
                      /*, @RequestParam("refreshToken") String refreshToken*/) {
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
    public UserResponse signUp(@RequestBody UserSignupRequest userSignupRequest) {
        // @RequestBody 어노테이션은 요청의 본문에 포함된 데이터를 AnnounceRequest 객체로 변환하여 announce 변수에 할당
        return userService.saveUser(userSignupRequest);
    }

    /**
     * 이메일 중복 확인
     */
    @PostMapping("/email/verify")
    public ResultResponse emailVerify(@RequestBody UserCheckRequest userCheckRequest) {
        return userService.emailVerify(userCheckRequest);
    }

    /**
     * 이메일 인증번호 전송
     */
    @Async
    @PostMapping("/email/request")
    public CompletableFuture<EmailtoClientResponse> sendVerifyEmail(@RequestBody EmailAddrRequest emailReq)
            throws MessagingException, UnsupportedEncodingException {
        emailCheck(emailReq);
        return emailService.sendEmail(emailReq);
    }

    /**
     * 이메일 인증번호 확인
     * 유저가 입력한 인증코드 받아서 이메일이랑 인증코드 같은지 확인하고 같을시 OK, 다를시 틀렸다고 알려줘야
     */
    @PutMapping("/email/response")
    public ResultResponse updateEmailAuth(@RequestBody EmailAuthUpdateRequest authReq) {
        return emailService.updateAuth(authReq);
    }

    @PutMapping("/password")
    public ResultResponse updatePwByToken(@RequestBody EmailUserPwRequest req){
        return userService.updatePwEmail(req);
    }

    @GetMapping("/nick/check")
    public ResultResponse checkNick(@RequestParam String usernick){
        return ResultResponse.toDto(userService.verifyRepeatedNick(usernick));
    }

    void emailCheck(EmailAddrRequest req) throws InvalidValueException {
        if (req.type.equals(EMAIL_JOIN)) {
            if (userService.isEmailExist(req.getEmail()))
                throw new AssertionError(ErrorCode.INVALID_EMAIL_EXIST);
        } else if (req.type.equals(EMAIL_PW)) {
            if (!userService.isEmailExist(req.getEmail()))
                throw new AssertionError(ErrorCode.INVALID_EMAIL_VALUE);
        } else {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
