package org.zerock.moamoa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.moamoa.domain.DTO.user.UserLoginRequest;
import org.zerock.moamoa.domain.DTO.user.UserLoginResponse;
import org.zerock.moamoa.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
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
}
