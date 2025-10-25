package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.AppointmentEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.codeit.roomunion.meeting.adapter.out.persistence.entity.QAppointmentEntity.appointmentEntity;
import static org.codeit.roomunion.meeting.adapter.out.persistence.entity.QAppointmentMemberEntity.appointmentMemberEntity;

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
}
