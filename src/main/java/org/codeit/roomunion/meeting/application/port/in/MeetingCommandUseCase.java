package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.command.MeetingUpdateCommand;
import org.springframework.web.multipart.MultipartFile;

public interface MeetingCommandUseCase {
    Meeting create(MeetingCreateCommand command, MultipartFile image);

    Meeting join(Long meetingId, Long userId);

    Meeting update(Long meetingId, Long currentId, MeetingUpdateCommand command, MultipartFile image);

    void deleteMeeting(Long meetingId, Long userId);
}
