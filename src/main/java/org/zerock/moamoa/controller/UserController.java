package org.zerock.moamoa.controller;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.common.auth.JwtTokenProvider;
import org.zerock.moamoa.common.email.EmailAuthRequest;
import org.zerock.moamoa.common.email.EmailAuthResponse;
import org.zerock.moamoa.common.email.EmailMessage;
import org.zerock.moamoa.common.email.EmailService;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.user.UserLoginRequest;
import org.zerock.moamoa.domain.DTO.user.UserLoginResponse;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.DTO.user.UserSignupRequest;
import org.zerock.moamoa.service.AuthService;
import org.zerock.moamoa.service.UserService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final AuthService authService;

    // 일단 만들어놓음 (나중에 백엔드에서 인증코드 비교하게 되면 사용)
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, LocalDateTime> verificationCodeExpiration = new HashMap<>(); // 유효 기간을 저장하는 맵
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/oauth")
    public Claims val(@RequestParam("accessToken") String accessToken,
                      @RequestParam("refreshToken") String refreshToken) {
        log.info("accessToken : " + accessToken);
        log.info("refreshToken : " + refreshToken);
        return jwtTokenProvider.parseClaims(accessToken);
    }

    /**
     * 토큰갱신 API
     */
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("refresh_token") String refreshToken) {
        String newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public UserResponse signUp(@RequestBody UserSignupRequest US) throws Exception {
        // @RequestBody 어노테이션은 요청의 본문에 포함된 데이터를 AnnounceRequest 객체로 변환하여 announce 변수에 할당
        return userService.saveUser(US);
    }

    /**
     * 이메일 인증번호 보냄
     */
    @PostMapping("/signup/sendemail")
    public ResponseEntity<?> sendVerifyEmail(@RequestBody EmailAuthRequest emailReq) throws MessagingException, UnsupportedEncodingException {
        // EmailAuthResponse
        // 이메일 중복 검사
        String email = emailReq.getEmail();
        if (userService.isEmailExist(email)) {
            throw new InvalidValueException(ErrorCode.INVALID_EMAIL_EXIST);
        }
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("모아모아에서 발급된 이메일 인증 코드입니다.")
                .build();

        String code = emailService.sendMail(emailMessage);
        EmailAuthResponse emailAuthRes = new EmailAuthResponse();
        emailAuthRes.setCode(code);

        // 임시
//		// EmailMessage의 email 설정
//		EmailMessage emailMessage = new EmailMessage();
//		emailMessage.setTo(emailReq.getEmail());
//
//		// 인증 코드 발송
//		String authCode = emailService.sendEmail(emailMessage);
//
//		// 인증 코드를 저장하여 나중에 비교할 수 있도록 함
//		verificationCodes.put(email, authCode);
//		// 유효 기간 설정 (현재 시간 + 15분)
//		LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(15);
//		verificationCodeExpiration.put(email, expirationTime);

        // 프론트엔드로 인증 코드 전송
        return ResponseEntity.ok(HttpStatus.OK);
//								emailAuthRes

    }
//
//	@PostMapping("verifyAuthCode")
//	public ResponseEntity<String> verifyAuthCode(@RequestBody EmailCodeRequest emailCodeReq) {
//		String email = emailCodeReq.getEmail();
//		String authCode = emailCodeReq.getCode();
//
//		// 유효 기간 확인
//		LocalDateTime expirationTime = verificationCodeExpiration.get(email);
//		if (expirationTime == null || LocalDateTime.now().isAfter(expirationTime)) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 코드의 유효 기간이 지났습니다.");
//		}
//
//		// 인증 코드 비교
//		String storedAuthCode = verificationCodes.get(email);
//		if (storedAuthCode.equals(authCode)) {
//			return ResponseEntity.ok("인증 성공");
//		} else {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
//		}
//	}

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/{id}")
    public Object deleteUser(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.ok(SuccessMessage.USER_DELETE);
    }

}
