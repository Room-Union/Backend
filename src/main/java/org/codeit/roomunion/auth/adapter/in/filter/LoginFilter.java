package org.codeit.roomunion.auth.adapter.in.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final long EXPIRATION;

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    public LoginFilter(
        JwtUtil jwtUtil,
        AuthenticationManager authenticationManager,
        @Value("${spring.jwt.access-token-expire-time}") long expiration
    ) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.objectMapper = new ObjectMapper();
        this.EXPIRATION = expiration;
        setFilterProcessesUrl("/v1/auth/login");
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
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ErrorCode errorCode = getErrorCode(failed);

        String errorResponse = objectMapper.writeValueAsString(createErrorResponse(errorCode));
        response.setStatus(errorCode.getStatusCode());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(errorResponse);
    }

    private Map<String, Object> createErrorResponse(ErrorCode errorCode) {
        return Map.of(
                "code", errorCode.getCode(),
                "message", errorCode.getMessage()
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
