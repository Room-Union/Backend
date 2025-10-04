package org.codeit.roomunion.user.adapter.out.persistence.jpa;

import org.codeit.roomunion.user.adapter.out.persistence.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVarificationJpaRepository extends JpaRepository<EmailVerificationEntity, Long> {
}
