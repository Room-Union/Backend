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

        return savedMeetingEntity.toDomain(hostUser.getId(), hostUser.getNickname());
    }


    @Override
    public Meeting findById(Long meetingId) {
        return meetingJpaRepository.findById(meetingId)
            .map(entity -> {
                return meetingMemberJpaRepository
                    .findBymeetingIdAndMeetingRole(meetingId, MeetingRole.HOST)
                    .map(mm -> {
                        Long hostUserId = mm.getUser().getId();
                        String hostNickname = mm.getUser().getNickname();
                        return entity.toDomain(hostUserId, hostNickname);
                    })
                    .orElseGet(() -> entity.toDomain(null, null));  // host 멤버 없을 때
            })
            .orElse(Meeting.getEmpty()); // 엔티티가 없을 때
    }

    @Override
    public boolean existsMeetingNameForHost(Long userId, String name) {
        return meetingMemberJpaRepository.existsMeetingByHostAndName(userId, name, MeetingRole.HOST);
    }
}
