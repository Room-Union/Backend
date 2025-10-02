package org.codeit.roomunion.moim.adapter.out.persistence.jpa;

import org.codeit.roomunion.moim.adapter.out.persistence.entity.CrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CrewJpaRepository extends JpaRepository<CrewEntity, Long> {
}
