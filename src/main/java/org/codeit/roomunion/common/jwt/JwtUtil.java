package org.codeit.roomunion.common.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Getter
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // Access Token 만료 시간: 1시간
    public static final long ACCESS_TOKEN_EXPIRATION = Duration.ofDays(7).toMillis();
    public static final long ACCESS_TOKEN2_EXPIRATION = Duration.ofSeconds(10).toMillis();

    // Refresh Token 만료 시간: 7일
    public static final long REFRESH_TOKEN_EXPIRATION = Duration.ofDays(7).toMillis();

    private SecretKey secretKey;

    @Value("${spring.jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(secret.getBytes(), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createAccessToken(Long userId, String email) {
        return createJwt(userId, email, ACCESS_TOKEN_EXPIRATION, "access");
    }

    public String createAccessToken2(Long userId, String email) {
        return createJwt(userId, email, ACCESS_TOKEN2_EXPIRATION, "access");
    }

    public String createRefreshToken(Long userId, String email) {
        return createJwt(userId, email, REFRESH_TOKEN_EXPIRATION, "refresh");
    }

    private String createJwt(Long userId, String email, Long expiration, String type) {
        return Jwts.builder()
            .claim("sub", userId.toString())
            .claim("email", email)
            .claim("type", type)
            .claim("jti", UUID.randomUUID().toString())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(secretKey)
            .compact();
    }

    public String createJwt(Long userId, String email, Long expiration) {
        return Jwts.builder()
            .claim("sub", userId.toString())
            .claim("email", email)
            .claim("jti", UUID.randomUUID().toString())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(secretKey)
            .compact();
    }
}
