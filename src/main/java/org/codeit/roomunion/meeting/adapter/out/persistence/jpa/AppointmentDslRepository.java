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

    /**
     * Appointment를 조회하면서 members도 함께 가져옴 (하나의 쿼리)
     * - Appointment 존재 여부 확인
     * - members 컬렉션을 통해 이미 멤버인지 확인 가능
     */
    public Optional<AppointmentEntity> findByIdWithMembers(Long appointmentId) {
        AppointmentEntity result = jpaQueryFactory
            .selectFrom(appointmentEntity)
            .leftJoin(appointmentEntity.members, appointmentMemberEntity).fetchJoin()
            .where(appointmentEntity.id.eq(appointmentId))
            .fetchOne();
        return Optional.ofNullable(result);
    }
}
