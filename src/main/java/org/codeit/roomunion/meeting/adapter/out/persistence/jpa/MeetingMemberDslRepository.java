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


    /**
     * meetingId와 userId로 MeetingMember가 존재하는지 확인 (fetchJoin 사용)
     * meeting 정보도 함께 로드됨
     */
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
