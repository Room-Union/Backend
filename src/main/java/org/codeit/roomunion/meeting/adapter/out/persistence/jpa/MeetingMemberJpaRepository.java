package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import java.util.Optional;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.domain.model.MeetingRole;
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
            select case when exists (
                select 1
                from MeetingMemberEntity mm
                join mm.meeting m
                where mm.meetingRole = :role
                  and mm.user.id = :userId
                  and lower(m.name) = lower(:name)
            ) then true else false end
        """)
    boolean existsMeetingByHostAndName(@Param("userId") Long userId,
                                       @Param("name") String name,
                                       @Param("role") MeetingRole role);

    int countByMeetingId(Long meetingId);

    // MeetingMemberEntity에 meeting_id, user_id로 매핑되어서 메소드명 이렇게 지어야함
    boolean existsByMeeting_IdAndUser_Id(Long meetingId, Long userId);

    @Query("""
            select case when exists (
                select 1
                from MeetingMemberEntity mm
                where mm.meeting.id = :meetingId
                  and mm.user.id = :userId
                  and mm.meetingRole = :role
            ) then true else false end
        """)
    boolean existsByMeetingIdAndUserIdAndRole(Long meetingId, Long userId, MeetingRole role);

    @Query("""
        select mm
        from MeetingMemberEntity mm
        where mm.meeting.id = :meetingId
         and mm.user.id = :userId
     """)
    MeetingMemberEntity findByMeetingIdAndUserId(
        @Param("meetingId") Long meetingId,
        @Param("userId") Long userId
    );
}
