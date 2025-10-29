package org.codeit.roomunion.meeting.adapter.out.persistence.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.QMeetingEntity;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.MeetingSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MeetingDslRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Meeting> searchByName(String name, MeetingCategory category, MeetingSort sort, int page, int size) {
        QMeetingEntity meeting = QMeetingEntity.meetingEntity;
        Pageable pageable = PageRequest.of(page, size);

        BooleanBuilder builder = new BooleanBuilder();
        if (name != null && !name.isBlank()) {
            builder.and(meeting.name.containsIgnoreCase(name.trim()));
        }
        if (category != null) {
            builder.and(meeting.category.eq(category));
        }

        OrderSpecifier<?>[] orderSpecifiers;

        switch (sort) {
            case MEMBER_DESC:
                orderSpecifiers = new OrderSpecifier<?>[]{
                    meeting.currentMemberCount.desc(),
                    meeting.createdAt.desc()};
                break;

            case LATEST:
            default:
                orderSpecifiers = new OrderSpecifier<?>[]{
                    meeting.createdAt.desc()};
                break;
        }

        List<MeetingEntity> contents = queryFactory
            .selectFrom(meeting)
            .where(builder)
            .orderBy(orderSpecifiers)
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
