package org.codeit.roomunion.auth.adapter.in.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.application.service.JwtService;
import org.codeit.roomunion.auth.domain.exception.AuthErrorCode;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.auth.domain.model.LoginUserDetails;
import org.codeit.roomunion.auth.domain.model.UnknownUserDetails;
import org.codeit.roomunion.common.exception.BaseErrorCode;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String jwtToken = resolveToken(request);

        // 토큰이 없으면 인증 세팅하지 말고 그냥 통과
        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 이미 인증된 상태면 패스
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }


        String email;
        try {
            email = jwtService.extractUsername(jwtToken);
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, AuthErrorCode.EXPIRED_JWT);
            return;
        } catch (MalformedJwtException e) {
            setErrorResponse(response, AuthErrorCode.MALFORMED_JWT);
            return;
        } catch (SignatureException e) {
            setErrorResponse(response, AuthErrorCode.INVALID_JWT_SIGNATURE);
            return;
        } catch (Exception e) {
            setErrorResponse(response, AuthErrorCode.INVALID_JWT);
            return;
        }

        LoginUserDetails userDetails =
            (LoginUserDetails) userDetailsService.loadUserByUsername(email);

        if (jwtService.isTokenInvalid(jwtToken, userDetails)) {
            setErrorResponse(response, AuthErrorCode.INVALID_JWT);
            return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    /**
     * 1순위: 쿠키 accessToken
     * 2순위: Authorization: Bearer ...
     */
    private String resolveToken(HttpServletRequest request) {
        // 쿠키 먼저
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 그다음 Authorization 헤더(Bearer)도 허용
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    private void setErrorResponse(HttpServletResponse response, BaseErrorCode errorCode) throws IOException {
        String errorResponse = objectMapper.writeValueAsString(createErrorResponse(errorCode));
        response.setStatus(errorCode.getStatusValue());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(errorResponse);
    }

    private Map<String, Object> createErrorResponse(BaseErrorCode errorCode) {
        return Map.of(
            "code", errorCode.getCode(),
            "message", errorCode.getMessage()
        );
    }
}