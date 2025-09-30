package org.codeit.roomunion.crew.adapter.out.persistence.jpa;

import aj.org.objectweb.asm.commons.Remapper;
import org.codeit.roomunion.crew.adapter.out.persistence.entity.CrewMemberEntity;
import org.codeit.roomunion.crew.domain.model.enums.CrewRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrewMemberJpaRepository extends JpaRepository<CrewMemberEntity, Long> {
    Optional<CrewMemberEntity> findByCrewIdAndCrewRole(Long crewId, CrewRole crewRole);
}
