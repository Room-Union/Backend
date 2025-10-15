package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingSort;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.data.domain.Page;

public interface MeetingQueryUseCase {

    Meeting getByMeetingId(Long meetingId, User user);

    Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, User user);
}
