package org.zerock.moamoa.common.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

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

    public String generateToken(UserDetails userDetails, Date expiryDate) {
        //Claims : JWT 에 담기는 정보의 단위를 Claim 이라고 부른다. JWT 에 담고싶은 사용자 정보를 담는다.
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(expiryDate);

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(jwtSecretKey)
                .compact();
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        Date expiryDate = new Date(new Date().getTime() + jwtAccessTokenExpirationTime);
        return generateToken(userDetails, expiryDate);
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        Date expiryDate = new Date(new Date().getTime() + jwtRefreshTokenExpirationTime);
        return generateToken(userDetails, expiryDate);
    }

    // 1. JWT가 유효한지 판단하는 메소드 jjwt 라이브러리에서는 JWT를 해석하는 과정에서 유효하지 않으면 예외가 발생
    public ValidResponse validate(String token) {
        Exception exception;

        try {
            // 정당한 JWT면 true,
            // parseClaimsJws: 암호화된 JWT를 해석하기 위한 메소드
            jwtParser.parseClaimsJws(token);
            // 정당하지 않은 JWT면 false
            return new ValidResponse(true);
        } catch (SignatureException ex) {
            exception = ex;
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            exception = ex;
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            exception = ex;
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            exception = ex;
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            exception = ex;
            System.out.println("JWT claims string is empty.");
        }
        return new ValidResponse(false, exception);
    }


    // JWT를 인자로 받고, 그 JWT를 해석해서 사용자 정보를 회수하는 메소드
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }
}
