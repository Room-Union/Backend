package org.codeit.roomunion.auth.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.jwt.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenInvalid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return !(username.equals(userDetails.getUsername()));
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
            .verifyWith(jwtUtil.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
<<<<<<< HEAD
=======
                .verifyWith(jwtUtil.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
    }
}