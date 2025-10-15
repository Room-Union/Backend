package org.codeit.roomunion.meeting.application.port.out;

import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingSort;
import org.springframework.data.domain.Page;

public interface MeetingRepository {
    Meeting createMeeting(MeetingCreateCommand command);

    Meeting findById(Long meetingId);

    boolean existsMeetingNameForHost(Long userId, String name);

    int countJoinedMembers(Long meetingId);

    Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size);

}
