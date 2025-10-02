package org.codeit.roomunion.meeting.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.exception.UserNotFoundException;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingJpaRepository;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingMemberJpaRepository;
import org.codeit.roomunion.meeting.application.port.out.MeetingRepository;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingRole;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepository {

    private final MeetingJpaRepository meetingJpaRepository;
    private final MeetingMemberJpaRepository meetingMemberJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Meeting createMeeting(MeetingCreateCommand command) {
        // TODO 현태님 UserErrorCode로 변경하시면 예외처리 적용
        UserEntity hostUser = userJpaRepository.findById(command.getUserId())
            .orElseThrow(UserNotFoundException::new);

        MeetingEntity meetingEntity = MeetingEntity.builder()
            .name(command.getName())
            .description(command.getDescription())
            .category(command.getCategory())
            .meetingImage(command.getImageUrl())
            .maxMemberCount(command.getMaxMemberCount())
            .platformUrls(command.getPlatformURL())
            .createdAt(LocalDateTime.now())
            .build();

        MeetingMemberEntity hostMember = MeetingMemberEntity.builder()
            .meeting(meetingEntity)
            .user(hostUser)
            .meetingRole(MeetingRole.HOST)
            .build();

        meetingEntity.addMember(hostMember);

        MeetingEntity savedMeetingEntity = meetingJpaRepository.save(meetingEntity);

        return savedMeetingEntity.toDomain(hostUser.getId());
    }


    @Override
    public Meeting findById(Long meetingId) {
        return meetingJpaRepository.findById(meetingId)
            .map(entity -> {
                // HOST 유저 찾기
                Long hostUserId = meetingMemberJpaRepository
                    .findBymeetingIdAndMeetingRole(meetingId, MeetingRole.HOST)
                    .map(meetingMemberEntity -> meetingMemberEntity.getUser().getId())
                    .orElse(null);
                return entity.toDomain(hostUserId);
            })
            .orElse(Meeting.getEmpty());
    }

    @Override
    public boolean existsMeetingNameForHost(Long userId, String name) {
        return meetingMemberJpaRepository.existsMeetingByHostAndName(userId, name, MeetingRole.HOST);
    }
}
