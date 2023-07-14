package org.zerock.moamoa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.moamoa.domain.DTO.user.UserLoginRequest;
import org.zerock.moamoa.domain.DTO.user.UserLoginResponse;
import org.zerock.moamoa.domain.DTO.user.UserResponse;
import org.zerock.moamoa.domain.DTO.user.UserSignupRequest;
import org.zerock.moamoa.service.AuthService;
import org.zerock.moamoa.service.EmailService;
import org.zerock.moamoa.service.EmailServiceImpl;
import org.zerock.moamoa.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final EmailService emailService;
	private final AuthService authService;

	/** 로그인 API */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
		UserLoginResponse response = authService.login(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/** 토큰갱신 API */
	@GetMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestHeader("refresh_token") String refreshToken) {
		String newAccessToken = authService.refreshToken(refreshToken);
		return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
	}

	/** 회원가입 */
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

}
