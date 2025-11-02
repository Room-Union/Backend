package org.codeit.roomunion.auth.domain.model;

import lombok.Getter;

/**
 * 로그인 결과 (Access Token + Refresh Token)
 */
@Getter
public class LoginResult {
    private final String accessToken;
    private final String accessToken2;
    private final String refreshToken;

    public LoginResult(String accessToken, String accessToken2, String refreshToken) {
        this.accessToken = accessToken;
        this.accessToken2 = accessToken2;
        this.refreshToken = refreshToken;
    }

    public static LoginResult of(String accessToken, String accessToken2, String refreshToken) {
        return new LoginResult(accessToken, accessToken2, refreshToken);
    }
}
