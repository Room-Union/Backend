package org.codeit.roomunion.auth.adapter.in.web.response;

/**
 * 토큰 응답 DTO
 * Refresh Token은 HttpOnly Cookie로 전달되므로 accessToken만 포함
 */
public record TokenResponse(String accessToken) {
    public static TokenResponse of(String accessToken) {
        return new TokenResponse(accessToken);
    }
}
