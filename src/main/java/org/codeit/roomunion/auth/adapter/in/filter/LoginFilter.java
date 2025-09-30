package org.codeit.roomunion.auth.adapter.in.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
<<<<<<< HEAD
import lombok.extern.slf4j.Slf4j;
=======
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.common.exception.ErrorCode;
import org.codeit.roomunion.common.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

<<<<<<< HEAD
@Slf4j
=======
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final long EXPIRATION;

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

<<<<<<< HEAD
<<<<<<< HEAD
    public LoginFilter(
        JwtUtil jwtUtil,
        AuthenticationManager authenticationManager
    ) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.objectMapper = new ObjectMapper();
        setFilterProcessesUrl("/v1/auth/login");
=======
    public LoginFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
=======
    public LoginFilter(
        JwtUtil jwtUtil,
        AuthenticationManager authenticationManager,
        @Value("${spring.jwt.access-token-expire-time}") long expiration
    ) {
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.objectMapper = new ObjectMapper();
<<<<<<< HEAD
        this.EXPIRATION = expiration;

        setFilterProcessesUrl("/auth/login");
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
        setFilterProcessesUrl("/v1/auth/login");
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        Long userId = customUserDetails.getId();
        String email = customUserDetails.getUsername();

        String token = jwtUtil.createJwt(userId, email, EXPIRATION);
        String bearer = "Bearer " + token;
        response.addHeader("Authorization", bearer);
<<<<<<< HEAD
<<<<<<< HEAD
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
=======
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ErrorCode errorCode = getErrorCode(failed);

        String errorResponse = objectMapper.writeValueAsString(createErrorResponse(errorCode));
        response.setStatus(errorCode.getStatusCode());
<<<<<<< HEAD
<<<<<<< HEAD
        response.setContentType("application/json; charset=UTF-8");
=======
        response.setContentType("application/json");
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
        response.setContentType("application/json; charset=UTF-8");
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))

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

    private ErrorCode getErrorCode(AuthenticationException failed) {
        return switch (failed) {
            case BadCredentialsException ignore -> ErrorCode.INVALID_INPUT_VALUE;
            case DisabledException ignore -> ErrorCode.LOGIN_FAIL;
            default -> ErrorCode.INTERNAL_SERVER_ERROR;
        };
    }
}
