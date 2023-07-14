package org.zerock.moamoa.config.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {
	@Value("${jwt.accessTokenExpirationTime}")
	private Long jwtAccessTokenExpirationTime;
	@Value("${jwt.refreshTokenExpirationTime}")
	private Long jwtRefreshTokenExpirationTime;

	private final Key jwtSecretKey;
	private final JwtParser jwtParser;

	public JwtTokenProvider(@Value("${jwt.secret}") String jwtSercet) {
		this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSercet.getBytes());
		this.jwtParser = Jwts.parserBuilder().setSigningKey(this.jwtSecretKey).build();
	}

	public String generateAccessToken(CustomUserDetails userDetails) {
		Date expiryDate = new Date(new Date().getTime() + jwtAccessTokenExpirationTime);
		//Claim : JWT 에 담길 데이터의 키 (맵의 키와 비슷한 용도)
		//이 부분은 JWT 에 담고싶은 사용자 정보를 담는다.
		Claims jwtClaims = Jwts.claims()
			.setSubject(userDetails.getUsername())
			.setIssuedAt(Date.from(Instant.now()))
			.setExpiration(expiryDate);

		return Jwts.builder()
			.setClaims(jwtClaims)
			.signWith(jwtSecretKey)
			.compact();
	}

	public String generateRefreshToken(CustomUserDetails userDetails) {
		Date expiryDate = new Date(new Date().getTime() + jwtRefreshTokenExpirationTime);
		//Claim : JWT 에 담길 데이터의 키 (맵의 키와 비슷한 용도)
		//이 부분은 JWT 에 담고싶은 사용자 정보를 담는다.
		Claims jwtClaims = Jwts.claims()
			.setSubject(userDetails.getUsername())
			.setIssuedAt(Date.from(Instant.now()))
			.setExpiration(expiryDate);

		return Jwts.builder()
			.setClaims(jwtClaims)
			.signWith(jwtSecretKey)
			.compact();
	}

	// 1. JWT가 유효한지 판단하는 메소드 jjwt 라이브러리에서는 JWT를 해석하는 과정에서 유효하지 않으면 예외가 발생
	public boolean validate(String token) {
		try {
			// 정당한 JWT면 true,
			// parseClaimsJws: 암호화된 JWT를 해석하기 위한 메소드
			jwtParser.parseClaimsJws(token);
			// 정당하지 않은 JWT면 false
			return true;
		} catch (Exception e) {
			log.warn("invalid jwt : {}", e.getClass());
			return false;
		}
	}

	// JWT를 인자로 받고, 그 JWT를 해석해서 사용자 정보를 회수하는 메소드
	public Claims parseClaims(String token) {
		return jwtParser
			.parseClaimsJws(token)
			.getBody();
	}
}
