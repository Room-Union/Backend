package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingSort;
import org.springframework.data.domain.Page;

public interface MeetingQueryUseCase {

    Meeting getByMeetingId(Long meetingId, Long currentUserId);

    Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, Long currentUserId);
}
