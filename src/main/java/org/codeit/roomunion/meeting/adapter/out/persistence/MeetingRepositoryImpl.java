package org.codeit.roomunion.meeting.adapter.out.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingJpaRepository;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingMemberJpaRepository;
import org.codeit.roomunion.meeting.application.port.out.MeetingRepository;
import org.codeit.roomunion.meeting.domain.command.MeetingUpdateCommand;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.MeetingRole;
import org.codeit.roomunion.meeting.domain.model.MeetingSort;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.codeit.roomunion.user.domain.exception.UserErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean existsMeetingById(Long meetingId) {
        return meetingJpaRepository.existsById(meetingId);
    }

    @Override
    public Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, Long currentUserId) {
        Pageable pageable = PageRequest.of(page, size);

        Page<MeetingEntity> resultPage = switch (sort) {
            case LATEST -> meetingJpaRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
            case MEMBER_DESC -> meetingJpaRepository.findByCategoryOrderByMemberCountDesc(category, pageable);
        };

        if (currentUserId == 0L || resultPage.isEmpty()) {
            return resultPage.map(e -> e.toDomain().withJoined(false));
        }

        List<Long> meetingIds = resultPage.stream()
            .map(MeetingEntity::getId)
            .toList();

        meetingJpaRepository.findAllWithPlatformUrlsByIdIn(meetingIds);

        Set<Long> joinedIds = new HashSet<>(meetingMemberJpaRepository.findJoinedMeetingIds(currentUserId, meetingIds));

        return resultPage.map(e -> e.toDomain().withJoined(joinedIds.contains(e.getId())));
    }

    @Override
    public Page<Meeting> findMyMeetings(MeetingRole role, int page, int size, Long currentUserId) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<MeetingEntity> resultPage = meetingJpaRepository.findByUserAndRole(currentUserId, role, pageable);

        return resultPage.map(e -> e.toDomain().withJoined(true));

    }

    @Override
    public Meeting findByIdWithJoined(Long meetingId, Long currentUserId) {
        MeetingEntity entity = meetingJpaRepository.findByIdWithMembers(meetingId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        boolean joined = isUserJoined(currentUserId, entity);

        //TODO : N+1 문제 발생하는데 나중에 해결 (platformUrl fetch join시 List 2개라 hibernate에서 예외터트림)
        return entity.toDomain().withJoined(joined);
    }

    /**
     * 특정 모임에 현재 유저가 포함되어 있는지 여부를 확인합니다.
     *
     * @param entity 조회된 모임 엔티티
     * @param currentUserId 현재 로그인한 유저의 ID
     * @return 유저가 모임 멤버로 포함되어 있으면 true, 아니면 false
     */
    private boolean isUserJoined(Long currentUserId, MeetingEntity entity) {
        if (currentUserId == 0L) return false;
        return entity.getMeetingMembers().stream() // 멤버 리스트 순회
            .anyMatch(meetingMemberEntity -> Objects.equals(meetingMemberEntity.getUser().getId(), currentUserId)); // anyMatch : 조건에 맞으면 true (모임 유저의 id와 currentUserId 비교)
    }

    @Override
    public boolean isMeetingMember(Long meetingId, Long userId) {
        return meetingMemberJpaRepository.existsByMeeting_IdAndUser_Id(meetingId, userId);
    }

    @Override
    public boolean isHostMember(Long meetingId, Long userId) {
        return meetingMemberJpaRepository.existsByMeetingIdAndUserIdAndRole(meetingId, userId, MeetingRole.HOST);
    }

    @Override
    public Meeting insertMember(Long meetingId, Long userId) {
        MeetingEntity meeting = meetingJpaRepository.findWithLockById(meetingId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));
        UserEntity user = userJpaRepository.findById(userId)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        MeetingMemberEntity memberEntity = MeetingMemberEntity.of(meeting, user);
        meeting.addMember(memberEntity);
        return meeting.toDomain().withJoined(true);
    }

    @Override
    @Transactional
    public Meeting updateMeeting(Long meetingId, MeetingUpdateCommand command) {
        MeetingEntity entity = meetingJpaRepository.findById(meetingId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        Meeting beforeDomain = entity.toDomain();
        Meeting afterDomain = beforeDomain.update(command);

        entity.applyFromDomain(afterDomain);
        return afterDomain;
    }

    @Override
    public void deleteMeeting(Long meetingId) {
        meetingJpaRepository.deleteById(meetingId);
    }
}
