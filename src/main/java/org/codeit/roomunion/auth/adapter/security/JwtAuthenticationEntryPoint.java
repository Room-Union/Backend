package org.codeit.roomunion.auth.adapter.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.codeit.roomunion.auth.domain.exception.AuthErrorCode;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {

        // 인증 실패(토큰 없음/만료로 쿠키 삭제 등)는 무조건 401로 통일
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");

        Map<String, Object> body = Map.of(
            "code", AuthErrorCode.UNAUTHORIZED_TOKEN.getCode(),
            "message", AuthErrorCode.UNAUTHORIZED_TOKEN.getMessage()
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}