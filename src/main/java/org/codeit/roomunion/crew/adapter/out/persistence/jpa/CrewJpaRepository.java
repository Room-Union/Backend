package org.codeit.roomunion.crew.adapter.out.persistence.jpa;

import org.codeit.roomunion.crew.adapter.out.persistence.entity.CrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CrewJpaRepository extends JpaRepository<CrewEntity, Long> {
}
