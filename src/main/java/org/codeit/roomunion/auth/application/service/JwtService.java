package org.codeit.roomunion.auth.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
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

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> Long.parseLong(claims.get("sub", String.class)));
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public boolean isRefreshToken(String token) {
        try {
            String tokenType = extractTokenType(token);
            return "refresh".equals(tokenType);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenInvalid(String token, UserDetails userDetails) {
        try {
            final Claims claims = extractAllClaims(token);
            final String username = claims.get("email", String.class);
            final Date expiration = claims.getExpiration();

            return !username.equals(userDetails.getUsername()) || expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (MalformedJwtException | SignatureException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Refresh Token 유효성 검증
     */
    public boolean validateRefreshToken(String token) {
        try {
            final Claims claims = extractAllClaims(token);
            final Date expiration = claims.getExpiration();
            final String tokenType = claims.get("type", String.class);

            // 만료 확인 및 타입 확인
            return expiration.after(new Date()) && "refresh".equals(tokenType);
        } catch (ExpiredJwtException e) {
            return false;
        } catch (MalformedJwtException | SignatureException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(jwtUtil.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}