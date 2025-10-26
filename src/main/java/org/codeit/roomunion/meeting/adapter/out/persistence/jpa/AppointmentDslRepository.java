package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.AppointmentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.codeit.roomunion.meeting.adapter.out.persistence.entity.QAppointmentEntity.appointmentEntity;
import static org.codeit.roomunion.meeting.adapter.out.persistence.entity.QAppointmentMemberEntity.appointmentMemberEntity;
import static org.codeit.roomunion.user.adapter.out.persistence.entity.QUserEntity.userEntity;

@Repository
public class AppointmentDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public AppointmentDslRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Optional<AppointmentEntity> findByIdWithMembers(Long appointmentId) {
        AppointmentEntity entity = jpaQueryFactory
            .selectFrom(appointmentEntity)
            .leftJoin(appointmentEntity.members, appointmentMemberEntity).fetchJoin()
            .where(appointmentEntity.id.eq(appointmentId))
            .fetchOne();
        return Optional.ofNullable(entity);
    }

    public List<AppointmentEntity> findAllBy(Long meetingId) {
        return jpaQueryFactory.selectFrom(appointmentEntity)
            .distinct()
            .leftJoin(appointmentEntity.members, appointmentMemberEntity).fetchJoin()
            .leftJoin(appointmentMemberEntity.user, userEntity).fetchJoin()
            .where(appointmentEntity.meeting.id.eq(meetingId))
            .orderBy(appointmentEntity.scheduledAt.desc())
            .fetch();
    }
}
