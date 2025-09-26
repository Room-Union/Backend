package org.codeit.roomunion.common.adapter.out.persistence.jpa;

import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UuidRepository extends JpaRepository<UuidEntity, Long> {
}
