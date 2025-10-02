package org.codeit.roomunion.meeting.application.service;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.adapter.out.persistence.jpa.UuidJpaRepository;
import org.codeit.roomunion.common.adapter.out.s3.AmazonS3Manager;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.application.port.in.MeetingCommandUseCase;
import org.codeit.roomunion.meeting.application.port.in.MeetingQueryUseCase;
import org.codeit.roomunion.meeting.application.port.out.MeetingRepository;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingService implements MeetingCommandUseCase, MeetingQueryUseCase {

    private final MeetingRepository meetingRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidJpaRepository uuidJpaRepository;

    @Override
    public Meeting create(MeetingCreateCommand command, MultipartFile image) {
        // 추후 예외는 ErrorCode로 변경 예정
        if (command.getMaxMemberCount() < 1) {
            throw new CustomException(MeetingErrorCode.INVALID_MAX_MEMBER_COUNT);
        }

        String normalizedName = command.getName().trim();
        if (meetingRepository.existsMeetingNameForHost(command.getUserId(), normalizedName)) {
            throw new CustomException(MeetingErrorCode.DUPLICATE_MEETING_NAME);
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            UuidEntity savedUuid = uuidJpaRepository.save(
                UuidEntity.builder().uuid(uuid).build()
            );
            imageUrl = s3Manager.uploadFile(s3Manager.meetingImageKey(savedUuid), image);
        }

        MeetingCreateCommand finalCommand = MeetingCreateCommand.builder()
            .name(command.getName())
            .description(command.getDescription())
            .category(command.getCategory())
            .maxMemberCount(command.getMaxMemberCount())
            .userId(command.getUserId())
            .platformURL(command.getPlatformURL())
            .imageUrl(imageUrl)
            .createdAt(LocalDateTime.now())
            .build();

        Meeting saved = meetingRepository.createMeeting(finalCommand);


        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Meeting getBymeetingId(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId);
        // 추후 예외는 ErrorCode로 변경 예정
        if (meeting.isEmpty()) {
            throw new CustomException(MeetingErrorCode.MEETING_NOT_FOUND);
        }
        return meeting;
    }
}
