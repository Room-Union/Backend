package org.codeit.roomunion.moim.adapter.out.persistence.jpa;

import org.codeit.roomunion.moim.adapter.out.persistence.entity.MoimEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MoimJpaRepository extends JpaRepository<MoimEntity, Long> {
}
