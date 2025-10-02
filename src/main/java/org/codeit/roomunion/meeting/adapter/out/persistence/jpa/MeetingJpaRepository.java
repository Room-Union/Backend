package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MeetingJpaRepository extends JpaRepository<MeetingEntity, Long> {
}
