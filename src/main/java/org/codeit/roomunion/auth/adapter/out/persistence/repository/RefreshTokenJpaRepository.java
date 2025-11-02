package org.codeit.roomunion.auth.adapter.out.persistence.repository;

import org.codeit.roomunion.auth.domain.model.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
