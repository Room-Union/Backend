package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.QMeetingEntity;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingDslRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Meeting> searchByName(String name, int page, int size) {
        QMeetingEntity meeting = QMeetingEntity.meetingEntity;
        Pageable pageable = PageRequest.of(page, size);

        BooleanExpression condition = (name == null || name.isBlank())
            ? null
            : meeting.name.containsIgnoreCase(name.trim());

        List<MeetingEntity> contents = queryFactory
            .selectFrom(meeting)
            .where(condition)
            .orderBy(meeting.createdAt.desc())
            .offset((long) page * size)
            .limit(size)
            .fetch();

        // 총 개수 필요할 시 해제
//        Long total = queryFactory
//            .select(meeting.count())
//            .from(meeting)
//            .where(condition)
//            .fetchOne();

        List<Meeting> meetings = contents.stream()
            .map(MeetingEntity::toDomain)
            .toList();

        return new PageImpl<>(meetings, pageable, 0);

    }
}
