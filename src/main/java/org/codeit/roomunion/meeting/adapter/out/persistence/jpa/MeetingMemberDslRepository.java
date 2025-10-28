package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.QMeetingMemberEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.codeit.roomunion.meeting.adapter.out.persistence.entity.QMeetingEntity.meetingEntity;

@Repository
public class MeetingMemberDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MeetingMemberDslRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Optional<MeetingMemberEntity> findBy(Long meetingId, Long userId) {
        MeetingMemberEntity meetingMemberEntity = jpaQueryFactory
            .selectFrom(QMeetingMemberEntity.meetingMemberEntity)
            .innerJoin(QMeetingMemberEntity.meetingMemberEntity.meeting, meetingEntity).fetchJoin()
            .where(
                QMeetingMemberEntity.meetingMemberEntity.meeting.id.eq(meetingId),
                QMeetingMemberEntity.meetingMemberEntity.user.id.eq(userId)
            )
            .fetchFirst();
        return Optional.ofNullable(meetingMemberEntity);
    }
}
