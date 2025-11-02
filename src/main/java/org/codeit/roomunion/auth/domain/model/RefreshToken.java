package org.codeit.roomunion.auth.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RefreshToken {

    private final Long id;
    private final String token;
    private final Long userId;
    private final LocalDateTime expiresAt;
    private final LocalDateTime createdAt;

    private RefreshToken(Long id, String token, Long userId, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public static RefreshToken of(Long id, String token, Long userId, LocalDateTime expiresAt, LocalDateTime createdAt) {
        return new RefreshToken(id, token, userId, expiresAt, createdAt);
    }

    public boolean isExpired(LocalDateTime currentAt) {
        return currentAt.isAfter(expiresAt);
    }
}
