package org.codeit.roomunion.auth.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.auth.adapter.in.web.request.LoginRequest;
import org.codeit.roomunion.auth.adapter.in.web.request.SendVerificationCodeRequest;
import org.codeit.roomunion.auth.adapter.in.web.request.VerifyEmailCodeRequest;
import org.codeit.roomunion.auth.adapter.in.web.response.TokenResponse;
import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.codeit.roomunion.auth.domain.exception.AuthErrorCode;
import org.codeit.roomunion.auth.domain.model.LoginResult;
import org.codeit.roomunion.auth.domain.model.RefreshResult;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/v1/auth")
@Slf4j
@Tag(name = "Auth API", description = "인증 API")
public class AuthController {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private static final int REFRESH_TOKEN_COOKIE_MAX_AGE = (int) (JwtUtil.REFRESH_TOKEN_EXPIRATION / 1000); // 7일
    private static final int ACCESS_TOKEN_COOKIE_MAX_AGE = (int) (JwtUtil.ACCESS_TOKEN_EXPIRATION / 1000); // 1시간

    @Value("${app.cookie.secure:true}")
    private boolean cookieSecure;

    private final AuthUseCase authUsecase;

    public AuthController(AuthUseCase authUsecase) {
        this.authUsecase = authUsecase;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
        @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {
        LoginResult loginResult = authUsecase.login(request.email(), request.password());

        Cookie accessTokenCookie = createAccessTokenCookie(loginResult.getAccessToken());
        Cookie refreshTokenCookie = createRefreshTokenCookie(loginResult.getRefreshToken(), response);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().build();
    }

    private Cookie createAccessTokenCookie(String accessToken) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(ACCESS_TOKEN_COOKIE_MAX_AGE);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }

    private Cookie createRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(REFRESH_TOKEN_COOKIE_MAX_AGE);
        cookie.setAttribute("SameSite", "Lax");
        return cookie;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        RefreshResult refreshResult = authUsecase.refresh(refreshToken);

        Cookie accessTokenCookie = createAccessTokenCookie(refreshResult.getAccessToken());
        response.addCookie(accessTokenCookie);
        return ResponseEntity.noContent().build();
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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        deleteCookie(ACCESS_TOKEN_COOKIE_NAME, "None", response);
        deleteCookie(REFRESH_TOKEN_COOKIE_NAME, "Lax", response);
        return ResponseEntity.noContent().build();
    }

    private void deleteCookie(String name, String sameSite, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", sameSite);
        response.addCookie(cookie);
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
