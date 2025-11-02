package org.codeit.roomunion.auth.application.port.out;

import org.codeit.roomunion.auth.domain.model.RefreshToken;

import java.time.LocalDateTime;

public interface RefreshTokenRepository {

    void save(Long userId, String refreshToken, LocalDateTime expiresAt);

    RefreshToken getRefreshToken(String token);

    void deleteByUserId(Long userId);
}
