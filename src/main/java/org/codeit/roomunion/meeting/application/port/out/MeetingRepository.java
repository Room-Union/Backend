package org.codeit.roomunion.meeting.application.port.out;

import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;

public interface MeetingRepository {
    Meeting createMeeting(MeetingCreateCommand command);

    void saveMeetingMemberAsHost(Long meetingId, Long userId);

    Meeting findById(Long meetingId);

    boolean existsMeetingNameForHost(Long userId, String name); // 해당 사용자가 같은 이름의 모임을 이미 보유하고 있는지

}
