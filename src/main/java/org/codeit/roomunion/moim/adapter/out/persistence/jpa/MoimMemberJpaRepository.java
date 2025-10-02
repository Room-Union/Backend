package org.codeit.roomunion.moim.adapter.out.persistence.jpa;

import org.codeit.roomunion.moim.adapter.out.persistence.entity.MoimMemberEntity;
import org.codeit.roomunion.moim.domain.model.enums.MoimRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MoimMemberJpaRepository extends JpaRepository<MoimMemberEntity, Long> {
    Optional<MoimMemberEntity> findByMoimIdAndgetMoimRole(Long moimId, MoimRole moimRole);

    @Query("""
            select (count(mm) > 0)
            from MoimMemberEntity mm
            join mm.moim m
            where mm.moimRole = :role
              and mm.user.id = :userId
              and lower(m.name) = lower(:name)
        """)
    boolean existsMoimByHostAndName(@Param("userId") Long userId,
                                    @Param("name") String name,
                                    @Param("role") MoimRole role);
}
