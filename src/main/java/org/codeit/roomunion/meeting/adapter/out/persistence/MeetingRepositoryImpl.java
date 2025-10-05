package org.codeit.roomunion.meeting.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.UserNotFoundException;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingJpaRepository;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingMemberJpaRepository;
import org.codeit.roomunion.meeting.application.port.out.MeetingRepository;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingRole;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

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

        MeetingEntity meetingEntity = MeetingEntity.from(command, hostUser);

        MeetingEntity savedMeetingEntity = meetingJpaRepository.save(meetingEntity);

        return savedMeetingEntity.toDomain(hostUser.getId(), hostUser.getNickname());
    }


    @Override
    public Meeting findById(Long meetingId) {
        MeetingEntity meeting = meetingJpaRepository.findById(meetingId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        MeetingMemberEntity host = meetingMemberJpaRepository
            .findByMeetingIdAndMeetingRole(meetingId, MeetingRole.HOST)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_HOST_NOT_FOUND));

        Long hostUserId = host.getUser().getId();
        String hostNickname = host.getUser().getNickname();

        return meeting.toDomain(hostUserId, hostNickname);
    }

    @Override
    public boolean existsMeetingNameForHost(Long userId, String name) {
        return meetingMemberJpaRepository.existsMeetingByHostAndName(userId, name, MeetingRole.HOST);
    }
}
