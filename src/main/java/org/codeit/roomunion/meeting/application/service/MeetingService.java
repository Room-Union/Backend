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
import org.codeit.roomunion.meeting.application.port.in.notification.NotificationUseCase;
import org.codeit.roomunion.meeting.application.port.out.MeetingRepository;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.command.MeetingUpdateCommand;
import org.codeit.roomunion.meeting.domain.command.notification.CreateAndSendNotificationCommand;
import org.codeit.roomunion.meeting.domain.model.*;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.domain.exception.UserErrorCode;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
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
    private final NotificationUseCase notificationUseCase;

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

        int maxRetry = 3;

        for(int i = 0; i < maxRetry; i++) {
            try {
                // 모임 가입 시도
                //    - MeetingEntity에 @Version이 걸려 있어
                //      동시에 다른 트랜잭션이 수정하면 OptimisticLockException 발생
                Meeting meeting = meetingRepository.insertMember(meetingId, userId);
                return getMeetingWithBadges(meeting);
            } catch (OptimisticLockingFailureException e) {
                // 낙관적 락 충돌 발생
                // → 다른 트랜잭션이 동일한 Meeting을 먼저 수정하여 버전이 변경됨
                // → 최신 상태 기준으로 다시 처리하기 위해 재시도

                if (i == maxRetry - 1) {
                    // 모든 재시도 실패 시
                    // -> 경쟁이 계속 발생한 경우이므로 충돌 에러 반환
                    throw new CustomException(MeetingErrorCode.JOIN_CONFLICT);
                }

                //아직 재시도 횟수가 남아있으면 다음 루프로 이동
                continue;
            } catch (DataIntegrityViolationException e) {
                // DB UNIQUE 제약 위반
                //    (meeting_id, user_id) 중복 → 이미 가입한 사용자
                //    → 서버 로직이 아닌 DB 차원에서 중복 가입 완전 차단
                throw new CustomException(MeetingErrorCode.ALREADY_JOINED);
            }
        }

        throw new CustomException(MeetingErrorCode.JOIN_CONFLICT);
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

        // 알림에 필요한 값은 삭제 전에 확보
        String meetingName = meeting.getName();
        String meetingImageUrl = meeting.getMeetingImage();

        // 삭제 알림 받을 대상자(모임 참여자) 목록 추출
        List<Long> targetUserIds = meetingRepository.getMeetingMembers(meetingId).stream()
                .map(meetingMemberEntity -> meetingMemberEntity.getUser().getId())
                    .distinct()
                    .toList();

        deleteMeetingImage(meeting.getMeetingImage());
        meetingRepository.deleteMeeting(meetingId);

        String message = "참여 중이던 " + meetingName + " 모임이 모임장에 의해 삭제되었습니다.";
        String targetUrl = "/gathering/detail/" + meetingId;

        for (Long targetId: targetUserIds) {
            notificationUseCase.createAndSend(
                CreateAndSendNotificationCommand.of(
                    targetId,
                    NotificationType.MEETING_DELETED,
                    message,
                    targetUrl,
                    meetingImageUrl
                )
            );
        }
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
