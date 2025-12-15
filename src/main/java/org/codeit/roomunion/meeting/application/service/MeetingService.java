package org.codeit.roomunion.meeting.application.service;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.common.adapter.out.s3.AmazonS3Manager;
import org.codeit.roomunion.common.application.port.out.UuidRepository;
import org.codeit.roomunion.common.domain.model.Uuid;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.adapter.in.web.response.MeetingMemberResponse;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.application.port.in.MeetingCommandUseCase;
import org.codeit.roomunion.meeting.application.port.in.MeetingQueryUseCase;
import org.codeit.roomunion.meeting.application.port.out.MeetingRepository;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.command.MeetingUpdateCommand;
import org.codeit.roomunion.meeting.domain.model.*;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
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
@Transactional(readOnly = true)
public class MeetingService implements MeetingCommandUseCase, MeetingQueryUseCase {

    private final MeetingRepository meetingRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidRepository uuidRepository;
    private final UserQueryUseCase userQueryUseCase;

    @Override
    @Transactional
    public Meeting create(MeetingCreateCommand command, MultipartFile image) {
        if (command.getMaxMemberCount() < 1) {
            throw new CustomException(MeetingErrorCode.INVALID_MAX_MEMBER_COUNT);
        }

        String normalizedName = command.getName().trim();
        if (meetingRepository.existsMeetingNameForHost(command.getUserId(), normalizedName)) {
            throw new CustomException(MeetingErrorCode.DUPLICATE_MEETING_NAME);
        }

        String imageUrl = null;
        imageUrl = uploadMeetingImage(image, imageUrl);

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
    public Meeting join(Long meetingId, CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        if (meetingRepository.isMeetingMember(meetingId, userId)) {
            throw new CustomException(MeetingErrorCode.ALREADY_JOINED);
        }

        Meeting meeting = meetingRepository.insertMember(meetingId, userId);
        return getMeetingWithBadges(meeting);
    }

    @Override
    @Transactional
    public Meeting update(Long meetingId, CustomUserDetails userDetails, MeetingUpdateCommand command, MultipartFile image) {
        Long currentId = userDetails.getUser().getId();
        Meeting meeting = meetingRepository.findByIdWithJoined(meetingId, currentId);

        if (!meeting.isHost(currentId)) {
            throw new CustomException(MeetingErrorCode.MEETING_MODIFY_FORBIDDEN);
        }

        String oldImageUrl = meeting.getMeetingImage();

        if (command.getRemoveImageUrl() != null && oldImageUrl.equals(command.getRemoveImageUrl())) {
            deleteMeetingImage(oldImageUrl);
            oldImageUrl = null;
        }

        String finalImageUrl = uploadMeetingImage(image, oldImageUrl);

        MeetingUpdateCommand commandWithImage = MeetingUpdateCommand.of(command, finalImageUrl);
        Meeting updatedMeeting = meetingRepository.updateMeeting(meetingId, commandWithImage);

        return getMeetingWithBadges(updatedMeeting);
    }

    private String uploadMeetingImage(MultipartFile image, String finalImageUrl) {
        if (image != null && !image.isEmpty()) {
            Uuid uuid = uuidRepository.save(Uuid.from(UUID.randomUUID().toString()));
            String key = Meeting.getImagePath(uuid.getValue());
            finalImageUrl = s3Manager.uploadFile(key, image);
        }
        return finalImageUrl;
    }

    private void deleteMeetingImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            s3Manager.deleteObjectByUrl(imageUrl);
        }
    }

    @Override
    @Transactional
    public void deleteMeeting(Long meetingId, CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        Meeting meeting = meetingRepository.findByIdWithJoined(meetingId, userId);

        if (!meeting.isHost(userId)) {
            throw new CustomException(MeetingErrorCode.MEETING_DELETE_FORBIDDEN);
        }

        deleteMeetingImage(meeting.getMeetingImage());

        meetingRepository.deleteMeeting(meetingId);
    }

    @Override
    @Transactional
    public String leave(Long meetingId, CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        Meeting meeting = meetingRepository.findByIdWithJoined(meetingId, userId);

        if (meeting.isHost(userId)) {
            throw new CustomException(MeetingErrorCode.MEETING_HOST_CANNOT_LEAVE);
        }

        meetingRepository.deleteMember(meetingId, userId);

        return meeting.getName();
    }

    @Override
    public Meeting getByMeetingId(Long meetingId, CustomUserDetails userDetails) {
        Long currentUserId = (userDetails != null && userDetails.isLoggedIn())
            ? userDetails.getUser().getId()
            : 0L;
        Meeting meeting = meetingRepository.findByIdWithJoined(meetingId, currentUserId);
        return getMeetingWithBadges(meeting);
    }


    @Override
    public Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, CustomUserDetails userDetails) {
        Long currentUserId = (userDetails != null && userDetails.isLoggedIn())
            ? userDetails.getUser().getId()
            : 0L;
        Page<Meeting> pageResult = meetingRepository.search(category, sort, page, size, currentUserId);
        return pageResult.map(this::getMeetingWithBadges);
    }

    @Override
    public Page<Meeting> getMyMeetings(MeetingRole role, int page, int size, CustomUserDetails userDetails) {
        Long currentUserId = userDetails.getUser().getId();
        Page<Meeting> pageResult = meetingRepository.findMyMeetings(role, page, size, currentUserId);
        return pageResult.map(this::getMeetingWithBadges);
    }
    @Override
    public boolean existsMemberBy(Long meetingId, User user) {
        return meetingRepository.existsMemberBy(meetingId, user);
    }

    @Override
    public Page<Meeting> searchByName(String name, MeetingCategory category, MeetingSort sort, int page, int size, CustomUserDetails userDetails) {
        String cleanedName = cleanSearchKeyword(name);
        Page<Meeting> pageResult = meetingRepository.searchByName(cleanedName, category, sort, page, size);
        return pageResult.map(this::getMeetingWithBadges);
    }

    @Override
    public List<MeetingMemberResponse> getMeetingMembers(Long meetingId) {
        boolean existsMeeting = meetingRepository.existsMeetingById(meetingId);

        if (!existsMeeting) {
            throw new CustomException(MeetingErrorCode.MEETING_NOT_FOUND);
        }

        List<MeetingMemberEntity> members = meetingRepository.getMeetingMembers(meetingId);

        return members.stream()
            .map(member -> {
                String profileImageUrl = getProfileImageUrl(member);
                return MeetingMemberResponse.from(member, profileImageUrl);
            })
            .toList();
    }

    private String getProfileImageUrl(MeetingMemberEntity member) {
        UserEntity user = member.getUser();
        String profileImageUrl = null;

        if (user.isHasImage()) {
            String key = String.format(User.PROFILE_IMAGE_PATH, user.getId());
            profileImageUrl = s3Manager.getUrlByKey(key);
        }
        return profileImageUrl;
    }

    private String cleanSearchKeyword(String name) {
        if (name == null) return "";

        String cleanedKeyword = name.trim();
        // 인젝션에 쓰일 수 있는 특수문자 정리
        cleanedKeyword = cleanedKeyword.replaceAll("[\\'\\\"\\;\\=\\(\\)\\[\\]\\{\\}]", ""); // 위험 문자 제거
        // LIKE 와일드카드 이스케이프
        cleanedKeyword = cleanedKeyword.replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_");
        return cleanedKeyword;
    }

    private Meeting getMeetingWithBadges(Meeting meeting) {
        List<MeetingBadge> badges = calculateBadges(meeting, LocalDateTime.now());
        return meeting.withBadges(badges);
    }

    private List<MeetingBadge> calculateBadges(Meeting meeting, LocalDateTime now) {
        List<MeetingBadge> badges = new ArrayList<>();
        LocalDateTime createdAt = meeting.getCreatedAt();

        int max = meeting.getMaxMemberCount();
        int currentCount = meeting.getCurrentMemberCount();

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
