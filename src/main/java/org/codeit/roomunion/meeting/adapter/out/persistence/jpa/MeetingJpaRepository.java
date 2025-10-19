package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
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

    @EntityGraph(attributePaths = {"meetingMembers", "meetingMembers.user"})
    @Query("""
          SELECT m
          FROM MeetingEntity m
          LEFT JOIN m.meetingMembers mm
          WHERE (:category IS NULL OR m.category = :category)
          GROUP BY m.id
          ORDER BY COUNT(mm.id) DESC, m.createdAt DESC
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

}
