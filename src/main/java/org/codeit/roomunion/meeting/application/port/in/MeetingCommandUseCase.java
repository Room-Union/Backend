package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.springframework.web.multipart.MultipartFile;

public interface MeetingCommandUseCase {
    Meeting create(MeetingCreateCommand command, MultipartFile image);

    Meeting join(Long meetingId, Long userId);
}
