package org.codeit.roomunion.auth.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Refresh Token JPA 엔티티
 */
@Entity
@Table(
    name = "refresh_tokens",
    indexes = {
        @Index(name = "idx_user_id", columnList = "userId"),
        @Index(name = "idx_token", columnList = "token")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Refresh Token 생성
     */
    public static RefreshTokenEntity of(Long userId, String token, LocalDateTime expiresAt) {
        return new RefreshTokenEntity(null, token, userId, expiresAt, LocalDateTime.now());
    }

    /**
     * Refresh Token이 만료되었는지 확인
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public RefreshToken toDomain() {
        return RefreshToken.of(id,  token, userId, expiresAt, createdAt);
    }
}
