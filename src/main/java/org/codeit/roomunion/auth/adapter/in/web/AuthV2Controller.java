package org.codeit.roomunion.auth.adapter.in.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.codeit.roomunion.auth.adapter.in.web.request.LoginRequest;
import org.codeit.roomunion.auth.adapter.in.web.response.TokenResponse;
import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.codeit.roomunion.auth.domain.model.LoginResult;
import org.codeit.roomunion.common.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/auth")
public class AuthV2Controller {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final int REFRESH_TOKEN_COOKIE_MAX_AGE = (int) (JwtUtil.REFRESH_TOKEN_EXPIRATION / 1000); // 7일

    private final AuthUseCase authUsecase;

    public AuthV2Controller(AuthUseCase authUsecase) {
        this.authUsecase = authUsecase;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResult loginResult = authUsecase.login(request.email(), request.password());

        Cookie refreshTokenCookie = createRefreshTokenCookie(loginResult.getRefreshToken(), response);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(TokenResponse.of(loginResult.getAccessToken(), loginResult.getAccessToken2()));
    }

    private Cookie createRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api");
        cookie.setMaxAge(REFRESH_TOKEN_COOKIE_MAX_AGE);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }
}
