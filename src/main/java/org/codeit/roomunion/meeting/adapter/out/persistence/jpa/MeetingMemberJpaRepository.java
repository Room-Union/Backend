package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingMemberJpaRepository extends JpaRepository<MeetingMemberEntity, Long> {

    @Query("""
            select mm.meeting.id
            from MeetingMemberEntity mm
            where mm.user.id = :userId
              and mm.meeting.id in :meetingIds
        """)
    List<Long> findJoinedMeetingIds(@Param("userId") Long userId,
                                    @Param("meetingIds") List<Long> meetingIds);

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

    int countByMeetingId(Long meetingId);

    @Query("""
            select (count(mm) > 0)
            from MeetingMemberEntity mm
            where mm.meeting.id = :meetingId
              and mm.user.id = :userId
        """)
    boolean existsByMeetingIdAndUserId(@Param("meetingId") Long meetingId,
                                       @Param("userId") Long userId);
}
