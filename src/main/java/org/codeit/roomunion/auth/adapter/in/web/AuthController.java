package org.codeit.roomunion.auth.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.codeit.roomunion.auth.adapter.in.web.request.SendVerificationCodeRequest;
import org.codeit.roomunion.auth.adapter.in.web.request.VerifyEmailCodeRequest;
import org.codeit.roomunion.auth.adapter.in.web.response.TokenResponse;
import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.codeit.roomunion.auth.domain.exception.AuthErrorCode;
import org.codeit.roomunion.auth.domain.model.RefreshResult;
import org.codeit.roomunion.common.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Auth API", description = "인증 API")
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private final AuthUseCase authUsecase;

    public AuthController(AuthUseCase authUsecase) {
        this.authUsecase = authUsecase;
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        RefreshResult refreshResult = authUsecase.refresh(refreshToken);
        return ResponseEntity.ok(TokenResponse.of(refreshResult.getAccessToken(), refreshResult.getAccessToken2()));
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    @PostMapping("/email/send")
    public ResponseEntity<Void> sendVerificationEmail(@RequestBody SendVerificationCodeRequest request) {
        authUsecase.sendVerificationCode(request.email());
        return ResponseEntity.noContent()
            .build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody VerifyEmailCodeRequest request) {
        authUsecase.verifyCode(request.email(), request.code());
        return ResponseEntity.noContent()
            .build();
    }

    @PostMapping("/email/extend")
    public ResponseEntity<Void> extendVerificationCode(@RequestBody SendVerificationCodeRequest request) {
        authUsecase.extendExpiration(request.email());
        return ResponseEntity.noContent()
            .build();
    }

}
