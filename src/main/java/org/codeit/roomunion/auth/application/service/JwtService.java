package org.codeit.roomunion.auth.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.jwt.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(jwtUtil.getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}