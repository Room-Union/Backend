package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.meeting.domain.model.Meeting;

public interface MeetingQueryUseCase {

    Meeting getByMeetingId(Long meetingId, Long currentUserId);
}
