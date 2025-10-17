package org.codeit.roomunion.meeting.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingJpaRepository;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingMemberJpaRepository;
import org.codeit.roomunion.meeting.application.port.out.MeetingRepository;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.command.MeetingUpdateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingRole;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingSort;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.codeit.roomunion.user.domain.exception.UserErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepository {

    private final MeetingJpaRepository meetingJpaRepository;
    private final MeetingMemberJpaRepository meetingMemberJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Meeting createMeeting(MeetingCreateCommand command) {
        UserEntity hostUser = userJpaRepository.findById(command.getUserId())
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        MeetingEntity meetingEntity = MeetingEntity.from(command, hostUser);

        MeetingEntity savedMeetingEntity = meetingJpaRepository.save(meetingEntity);

        return savedMeetingEntity.toDomain();
    }


    @Override
    public Meeting findById(Long meetingId) {
        MeetingEntity meeting = meetingJpaRepository.findById(meetingId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        return meeting.toDomain();
    }

    @Override
    public boolean existsMeetingNameForHost(Long userId, String name) {
        return meetingMemberJpaRepository.existsMeetingByHostAndName(userId, name, MeetingRole.HOST);
    }

    @Override
    public int countJoinedMembers(Long meetingId) {
        return meetingMemberJpaRepository.countByMeetingId(meetingId);
    }

    @Override
    public Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, Long currentUserId) {
        Pageable pageable = PageRequest.of(page, size);

        Page<MeetingEntity> resultPage = switch (sort) { //목록 쿼리 1회
            case LATEST -> meetingJpaRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
            case MEMBER_DESC -> meetingJpaRepository.findByCategoryOrderByMemberCountDesc(category, pageable);
        };

        // 로그인 안 했거나 결과가 없으면 전부 joined=false
        if (currentUserId == null || resultPage.isEmpty()) {
            return resultPage.map(e -> e.toDomain().withJoined(false));
        }

        // 페이지 내 meetingId 모아서 한 방에 내가 가입한 ID들 조회
        List<Long> meetingIds = resultPage.map(MeetingEntity::getId).getContent();
        Set<Long> joinedIds = new HashSet<>(meetingMemberJpaRepository.findJoinedMeetingIds(currentUserId, meetingIds)); // joined 한 방 쿼리 1회 (총 쿼리 2회 고정)

        // joined는 메모리에서 contains로만 판정 (추가 쿼리 없음)
        return resultPage.map(e -> e.toDomain().withJoined(joinedIds.contains(e.getId())));
    }

    @Override
    public Meeting findByIdWithJoined(Long meetingId, Long currentUserId) {
        MeetingEntity entity = meetingJpaRepository.findByIdWithMembers(meetingId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        boolean joined = currentUserId != null && entity.getMeetingMembers().stream()
            .anyMatch(mm -> mm.getUser() != null && Objects.equals(mm.getUser().getId(), currentUserId));

        return entity.toDomain().withJoined(joined);
    }

    @Override
    public boolean isMeetingMember(Long meetingId, Long userId) {
        return meetingMemberJpaRepository.existsByMeetingIdAndUserId(meetingId, userId);
    }

    @Override
    public void insertMember(Long meetingId, Long userId, MeetingRole role) {
        MeetingEntity meeting = meetingJpaRepository.findById(meetingId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));
        UserEntity user = userJpaRepository.findById(userId)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        if (meetingMemberJpaRepository.existsByMeetingIdAndUserId(meetingId, userId)) {
            throw new CustomException(MeetingErrorCode.ALREADY_JOINED);
        }

        MeetingMemberEntity mm = MeetingMemberEntity.of(meeting, user, role);
        meeting.addMember(mm);
        meetingMemberJpaRepository.save(mm);
    }

    @Override
    @Transactional
    public Meeting updateMeeting(Long meetingId, MeetingUpdateCommand command) {
        MeetingEntity entity = meetingJpaRepository.findById(meetingId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        int currentCount = meetingMemberJpaRepository.countByMeetingId(meetingId);

        // 엔티티 내부 불변성 검증 + 전체 치환
        entity.replaceAllFrom(command, currentCount);

        // Dirty checking으로 반영 → 도메인으로 변환
        return entity.toDomain();
    }
}
