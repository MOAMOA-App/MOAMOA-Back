package org.zerock.moamoa.common.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        ValidResponse validResponse = jwtTokenProvider.validate(token);
        //인증된 토큰인 경우
        if (validResponse.isValid) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            // JWT에서 사용자 이름을 가져오기
            // 이메일
            String username = jwtTokenProvider.parseClaims(token).getSubject();
            // 사용자 인증 정보 생성
            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    token,
                    new ArrayList<>()
            );

            // SecurityContext에 사용자 정보 설정
            context.setAuthentication(authenticationToken);

            // SecurityContextHolder에 SecurityContext 설정
            SecurityContextHolder.setContext(context);
            log.info("JWT 를 위한 security context 를 설정했습니다.");

        } else if (validResponse.getException() instanceof ExpiredJwtException) {
            response.getWriter().write("JWT Token Expired");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.split(" ")[1];
        }
        return "";
    }

}