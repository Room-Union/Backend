package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import java.util.List;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.MeetingRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MeetingJpaRepository extends JpaRepository<MeetingEntity, Long> {

    @EntityGraph(attributePaths = {"meetingMembers", "meetingMembers.user"})
    @Query("""
          SELECT m
          FROM MeetingEntity m
          WHERE (:category IS NULL OR m.category = :category)
          ORDER BY m.createdAt DESC
        """)
    Page<MeetingEntity> findByCategoryOrderByCreatedAtDesc(
        @Param("category") MeetingCategory category,
        Pageable pageable
    );

    // 페이지네이션 환경에서 @EntityGraph + 루트 쿼리의 집계 정렬(GROUP BY/COUNT) 이 컬렉션 로딩에 영향 끼쳐서 컬렉션이 비어짐.
    // -> 정렬을 서브쿼리로 해서 해결
    @EntityGraph(attributePaths = {"meetingMembers", "meetingMembers.user"})
    @Query("""
          SELECT m
          FROM MeetingEntity m
          WHERE (:category IS NULL OR m.category = :category)
          ORDER BY (
              SELECT COUNT(mm)
              FROM MeetingMemberEntity mm
              WHERE mm.meeting = m
          ) DESC, m.createdAt DESC
        """)
    Page<MeetingEntity> findByCategoryOrderByMemberCountDesc(
        @Param("category") MeetingCategory category,
        Pageable pageable
    );

    @Query("""
            select distinct m
            from MeetingEntity m
            left join fetch m.meetingMembers mm
            left join fetch mm.user u
            where m.id = :meetingId
        """)
    Optional<MeetingEntity> findByIdWithMembers(@Param("meetingId") Long meetingId);

    @Query("""
            select distinct m
            from MeetingEntity m
            left join fetch m.platformUrls pu
            where m.id in :ids
        """)
    List<MeetingEntity> findAllWithPlatformUrlsByIdIn(@Param("ids") List<Long> ids);


    @EntityGraph(attributePaths = {"meetingMembers", "meetingMembers.user"})
    @Query("""
          select m
          from MeetingEntity m
          join m.meetingMembers mm
          where mm.user.id = :userId
            and mm.meetingRole = :role
          order by m.createdAt desc
       """)
    Page<MeetingEntity> findByUserAndRole(
        @Param("userId") Long userId,
        @Param("role") MeetingRole role,
        Pageable pageable
    );

}
