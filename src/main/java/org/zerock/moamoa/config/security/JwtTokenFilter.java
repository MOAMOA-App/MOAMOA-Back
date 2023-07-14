package org.zerock.moamoa.config.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		// JWT가 포함되어 있으면 포함되어 있는 헤더를 요청
		String authHeader
			= request.getHeader(HttpHeaders.AUTHORIZATION);
		// authHeader가 null이 아니면서 "Bearer " 로 구성되어 있어야
		// 정상적인 인증 정보다.
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			// JWT를 회수하여 JWT가 정상적인 JWT인지를 판단한다.
			String token = authHeader.split(" ")[1];
			if (jwtTokenProvider.validate(token)) {
				// 웹상의 많은 예시
				//                SecurityContextHolder.getContext().setAuthentication();
				// Security 공식 문서 추천
				SecurityContext context
					= SecurityContextHolder.createEmptyContext();
				// JWT에서 사용자 이름을 가져오기
				String username = jwtTokenProvider
					.parseClaims(token)
					.getSubject();
				// 사용자 인증 정보 생성
				AbstractAuthenticationToken authenticationToken
					= new UsernamePasswordAuthenticationToken(
					CustomUserDetails.builder()
						.username(username)
						.build(),
					token, new ArrayList<>()
				);
				// SecurityContext에 사용자 정보 설정
				context.setAuthentication(authenticationToken);
				// SecurityContextHolder에 SecurityContext 설정
				SecurityContextHolder.setContext(context);
				log.info("set security context with jwt");
			}
			// 아니라면 log.warn을 통해 알린다.
			else {
				log.warn("jwt validation failed");
			}
		}
		filterChain.doFilter(request, response);
	}
}
