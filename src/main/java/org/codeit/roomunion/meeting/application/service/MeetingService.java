package org.codeit.roomunion.meeting.application.service;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.common.adapter.out.s3.AmazonS3Manager;
import org.codeit.roomunion.common.application.port.out.UuidRepository;
import org.codeit.roomunion.common.domain.model.Uuid;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.application.port.in.MeetingCommandUseCase;
import org.codeit.roomunion.meeting.application.port.in.MeetingQueryUseCase;
import org.codeit.roomunion.meeting.application.port.out.MeetingRepository;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.MeetingBadge;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.MeetingSort;
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
            String key = Meeting.getImagePath(uuid.getValue());
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
    @Transactional(readOnly = true)
    public Meeting getByMeetingId(Long meetingId, CustomUserDetails userDetails) {
        Meeting meeting = meetingRepository.findById(meetingId);

        boolean isJoined = isUserJoined(userDetails, meeting);
        meeting = meeting.withJoined(isJoined);

        return getMeetingWithBadges(meeting);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, CustomUserDetails userDetails) {
        Page<Meeting> pageResult = meetingRepository.search(category, sort, page, size);

        return pageResult
            .map(meeting -> {
                boolean isHost = isUserJoined(userDetails, meeting);
                return getMeetingWithBadges(meeting.withJoined(isHost));
            });
    }

    private boolean isUserJoined(CustomUserDetails userDetails, Meeting meeting) {
        if (!userDetails.isLoggedIn()) {
            return false;
        }
        // 가입 API 구현 이후 : return meetingMemberJpaRepository.existsByMeetingIdAndUserId(meeting.getId(), currentUserId);
        return Objects.equals(userDetails.getUser(), meeting.getHost());
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

        if (currentCount >= max) {
            badges.add(MeetingBadge.CLOSED);
            return badges;
        }

        if (currentCount < max) {
            badges.add(MeetingBadge.RECRUITING);
        }

        if (createdAt != null && !createdAt.isBefore(now.minusDays(7))) {
            badges.add(MeetingBadge.NEW);
        }

        int remaining = max - currentCount;
        int closingLimit = (int) Math.ceil(meeting.getMaxMemberCount() / 5.0);

        if (remaining > 0 && remaining <= closingLimit) {
            badges.add(MeetingBadge.CLOSING_SOON);
        }

        return badges;

    }
}
