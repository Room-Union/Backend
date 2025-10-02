package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MeetingMemberJpaRepository extends JpaRepository<MeetingMemberEntity, Long> {
    Optional<MeetingMemberEntity> findBymeetingIdAndMeetingRole(Long meetingId, MeetingRole meetingRole);

    @Query("""
            select (count(mm) > 0)
            from MeetingMemberEntity mm
            join mm.meeting m
            where mm.meetingRole = :role
              and mm.user.id = :userId
              and lower(m.name) = lower(:name)
        """)
    boolean existsMeetingByHostAndName(@Param("userId") Long userId,
                                       @Param("name") String name,
                                       @Param("role") MeetingRole role);
}
