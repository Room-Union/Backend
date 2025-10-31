package org.codeit.roomunion.auth.adapter.out.persistence;

import org.codeit.roomunion.auth.adapter.out.persistence.repository.RefreshTokenJpaRepository;
import org.codeit.roomunion.auth.application.port.out.RefreshTokenRepository;
import org.codeit.roomunion.auth.domain.exception.AuthErrorCode;
import org.codeit.roomunion.auth.domain.model.RefreshToken;
import org.codeit.roomunion.auth.domain.model.RefreshTokenEntity;
import org.codeit.roomunion.common.exception.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Transactional(readOnly = true)
public class JpaRefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    public JpaRefreshTokenRepositoryAdapter(RefreshTokenJpaRepository refreshTokenJpaRepository) {
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
    }

    @Override
    @Transactional
    public void save(Long userId, String refreshToken, LocalDateTime expiresAt) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(userId, refreshToken, expiresAt);
        refreshTokenJpaRepository.findByUserId(refreshTokenEntity.getUserId())
                .ifPresent(refreshTokenJpaRepository::delete);
        refreshTokenJpaRepository.save(refreshTokenEntity);
    }

    @Override
    public RefreshToken getRefreshToken(String token) {
        return refreshTokenJpaRepository.findByToken(token)
            .map(RefreshTokenEntity::toDomain)
            .orElseThrow(() -> new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenJpaRepository.deleteByUserId(userId);
    }
}
