package org.codeit.roomunion.meeting.application.service;

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
import org.codeit.roomunion.meeting.domain.model.command.MeetingUpdateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingBadge;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingRole;
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
    @Transactional
    public Meeting join(Long meetingId, Long userId) {
        if (meetingRepository.isMeetingMember(meetingId, userId)) {
            Meeting joined = meetingRepository.findByIdWithJoined(meetingId, userId);
            return getMeetingWithBadges(joined);
        }

        meetingRepository.insertMember(meetingId, userId, MeetingRole.MEMBER);

        Meeting joinedMeeting = meetingRepository.findByIdWithJoined(meetingId, userId);
        return getMeetingWithBadges(joinedMeeting);

    }

    @Override
    public Meeting update(Long meetingId, Long currentId, MeetingUpdateCommand command, MultipartFile image) {
        Meeting meeting = meetingRepository.findById(meetingId);

        boolean isHost = meetingRepository.isHostMember(meetingId, currentId);
        if (!isHost) {
            throw new CustomException(MeetingErrorCode.MEETING_MODIFY_FORBIDDEN);
        }

        String finalImageUrl = meeting.getMeetingImage();
        if (image != null && !image.isEmpty()) {
            Uuid uuid = uuidRepository.save(Uuid.from(UUID.randomUUID().toString()));
            String key = Meeting.getImagePath(uuid.getValue());
            finalImageUrl = s3Manager.uploadFile(key, image);
        }

        MeetingUpdateCommand commandWithImage = MeetingUpdateCommand.of(command, finalImageUrl);
        meetingRepository.updateMeeting(meetingId, commandWithImage);

        Meeting joinedMeeting = meetingRepository.findByIdWithJoined(meetingId, currentId);
        return getMeetingWithBadges(joinedMeeting);
    }

    @Override
    public void deleteMeeting(Long meetingId, Long userId) {
        meetingRepository.findById(meetingId);
        if (!meetingRepository.existsMeetingById(meetingId)) {
            throw new CustomException(MeetingErrorCode.MEETING_NOT_FOUND);
        }

        boolean isHost = meetingRepository.isHostMember(meetingId, userId);
        if (!isHost) {
            throw new CustomException(MeetingErrorCode.MEETING_DELETE_FORBIDDEN);
        }

        meetingRepository.deleteMeeting(meetingId);
    }

    @Override
    @Transactional(readOnly = true)
    public Meeting getByMeetingId(Long meetingId, Long currentUserId) {
        Meeting meeting = meetingRepository.findByIdWithJoined(meetingId, currentUserId);
        return getMeetingWithBadges(meeting);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size,
                                Long currentUserId) {
        Page<Meeting> pageResult = meetingRepository.search(category, sort, page, size, currentUserId);
        return pageResult.map(this::getMeetingWithBadges);
    }


    private Meeting getMeetingWithBadges(Meeting meeting) {
        int currentCount = meeting.getCurrentMemberCount();
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
