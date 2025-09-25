package org.codeit.roomunion.auth.adapter.in.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.application.service.JwtService;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.common.exception.ErrorCode;
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

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
<<<<<<< HEAD
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
=======
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (isNotBearerToken(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);
        String email = jwtService.extractUsername(jwtToken);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

            validateToken(response, jwtToken, userDetails);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
<<<<<<< HEAD
                userDetails,
                null,
                userDetails.getAuthorities()
=======
                    userDetails,
                    null,
                    userDetails.getAuthorities()
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private void validateToken(HttpServletResponse response, String jwtToken, CustomUserDetails userDetails) throws IOException {
        if (jwtService.isTokenInvalid(jwtToken, userDetails)) {
            setErrorResponse(response, ErrorCode.INVALID_JWT);
            throw new JwtException("잘못된 토큰입니다.");
        }
        if (jwtService.isTokenExpired(jwtToken)) {
            setErrorResponse(response, ErrorCode.EXPIRED_JWT);
            throw new JwtException("만료된 토큰입니다.");
        }
    }

    private boolean isNotBearerToken(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        String errorResponse = objectMapper.writeValueAsString(createErrorResponse(errorCode));
        response.setStatus(errorCode.getStatusCode());
<<<<<<< HEAD
        response.setContentType("application/json; charset=UTF-8");
=======
        response.setContentType("application/json");
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))

        response.getWriter().write(errorResponse);
    }

    private Map<String, Object> createErrorResponse(ErrorCode errorCode) {
        return Map.of(
<<<<<<< HEAD
            "code", errorCode.getCode(),
            "message", errorCode.getMessage()
=======
                "code", errorCode.getCode(),
                "message", errorCode.getMessage()
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
        );
    }
}