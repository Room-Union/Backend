package org.codeit.roomunion.user.adapter.out.persistence.jpa;

import org.codeit.roomunion.user.adapter.out.persistence.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationEntity, Long> {

    @Query("SELECT e FROM email_verifications e " +
        "WHERE e.email = :email " +
        "AND e.expirationAt > :currentAt " +
        "ORDER BY e.createdAt DESC " +
        "LIMIT 1 ")
    Optional<EmailVerificationEntity> findValidVerificationBy(String email, LocalDateTime currentAt);

    Optional<EmailVerificationEntity> findByEmail(String email);

    @Query("SELECT e FROM email_verifications e " +
        "WHERE e.email = :email " +
        "ORDER BY e.createdAt DESC " +
        "LIMIT 1 ")
    Optional<EmailVerificationEntity> findLatestVerificationByEmail(String email);
}
