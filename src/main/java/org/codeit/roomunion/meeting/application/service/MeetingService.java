package org.codeit.roomunion.meeting.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.adapter.out.s3.AmazonS3Manager;
import org.codeit.roomunion.common.application.port.out.UuidRepository;
import org.codeit.roomunion.common.domain.model.Uuid;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.application.port.in.MeetingCommandUseCase;
import org.codeit.roomunion.meeting.application.port.in.MeetingQueryUseCase;
import org.codeit.roomunion.meeting.application.port.out.MeetingRepository;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingBadge;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingSort;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.domain.exception.UserErrorCode;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingService implements MeetingCommandUseCase, MeetingQueryUseCase {

    private final MeetingRepository meetingRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final UserQueryUseCase userQueryUseCase;

    @Override
    public Meeting create(MeetingCreateCommand command, MultipartFile image) {
        if (command.getMaxMemberCount() < 1) {
            throw new CustomException(MeetingErrorCode.INVALID_MAX_MEMBER_COUNT);
        }

        String normalizedName = command.getName().trim();
        if (meetingRepository.existsMeetingNameForHost(command.getUserId(), normalizedName)) {
            throw new CustomException(MeetingErrorCode.DUPLICATE_MEETING_NAME);
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            Uuid uuid = uuidRepository.save(Uuid.from(UUID.randomUUID().toString()));
            String key = Meeting.getImagePath(uuid.getValue()); // 도메인에서 경로 생성
            imageUrl = s3Manager.uploadFile(key, image);
        }

        User host = userQueryUseCase.findByEmail(command.getHostEmail())
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        MeetingCreateCommand finalCommand = MeetingCreateCommand.of(command, imageUrl);

        Meeting meeting = meetingRepository.createMeeting(finalCommand)
            .withHost(host)
            .withJoined(true);

        return getMeetingWithBadges(meeting);
    }

    @Override
    public Meeting join(Long meetingId, Long userId) {

    }

    @Override
    @Transactional(readOnly = true)
    public Meeting getByMeetingId(Long meetingId, Long currentUserId) {
        Meeting meeting = meetingRepository.findById(meetingId);

        // TODO 현재는 호스트만 isJoined = true (모임 가입 API 도입 후 변경 예정)
        boolean isHost =  currentUserId != null && Objects.equals(meeting.getHost().getId(), currentUserId);
        meeting = meeting.withJoined(isHost);

        return getMeetingWithBadges(meeting);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size,
                                Long currentUserId) {
        Page<Meeting> pageResult = meetingRepository.search(category, sort, page, size);

        // TODO 현재는 호스트만 isJoined = true (모임 가입 API 도입 후 변경 예정)
        return pageResult
            .map(meeting -> {
                boolean isHost = isUserJoined(currentUserId, meeting);
                return getMeetingWithBadges(meeting.withJoined(isHost));
            });
    }

    private boolean isUserJoined(Long currentUserId, Meeting meeting) {
        if (currentUserId == null) {
            return false;
        }

        Long hostId = (meeting.getHost() != null) ? meeting.getHost().getId() : null;
        // 가입 API 구현 이후 : return meetingMemberJpaRepository.existsByMeetingIdAndUserId(meeting.getId(), currentUserId);
        return hostId != null && Objects.equals(currentUserId, hostId);
    }

    private Meeting getMeetingWithBadges(Meeting meeting) {
        int currentCount = meetingRepository.countJoinedMembers(meeting.getId());
        List<MeetingBadge> badges = calculateBadges(meeting, currentCount, LocalDateTime.now());
        return meeting.withBadges(badges);
    }


    private List<MeetingBadge> calculateBadges(Meeting meeting, int currentCount, LocalDateTime now) {
        List<MeetingBadge> badges = new ArrayList<>();
        LocalDateTime createdAt = meeting.getCreatedAt();

        int max = meeting.getMaxMemberCount();

        // 마감 여부 확인
        if (currentCount >= max) {
            badges.add(MeetingBadge.CLOSED);
            return badges;
        }

        // 모집중
        if (currentCount < max) {
            badges.add(MeetingBadge.RECRUITING);
        }

        // 신규 (7일)
        if (createdAt != null && !createdAt.isBefore(now.minusDays(7))) {
            badges.add(MeetingBadge.NEW);
        }

        // 마감임박: 최대인원 5명당 마감임박기준을 1명
        // ex) 최대 12명 중 3명 이하 남음(5명당 1명 기준) → 마감임박
        int remaining = max - currentCount; // 12 - 10 = 2
        int closingLimit = (int) Math.ceil(meeting.getMaxMemberCount() / 5.0); // ceil(12 / 5.0) = 3

        if (remaining > 0 && remaining <= closingLimit) { // 2 <= 3 → true
            badges.add(MeetingBadge.CLOSING_SOON);
        }

        return badges;

    }
}
