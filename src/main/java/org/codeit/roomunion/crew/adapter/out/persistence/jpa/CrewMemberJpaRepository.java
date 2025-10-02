package org.codeit.roomunion.crew.adapter.out.persistence.jpa;

import org.codeit.roomunion.crew.adapter.out.persistence.entity.CrewMemberEntity;
import org.codeit.roomunion.crew.domain.model.enums.CrewRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CrewMemberJpaRepository extends JpaRepository<CrewMemberEntity, Long> {
    Optional<CrewMemberEntity> findByCrewIdAndCrewRole(Long crewId, CrewRole crewRole);

    @Query("""
            select (count(cm) > 0)
            from CrewMemberEntity cm
            join cm.crew c
            where cm.crewRole = :role
              and cm.user.id = :userId
              and lower(c.name) = lower(:name)
        """)
    boolean existsHostCrewName(@Param("userId") Long userId,
                               @Param("name") String name,
                               @Param("role") CrewRole role);
}
