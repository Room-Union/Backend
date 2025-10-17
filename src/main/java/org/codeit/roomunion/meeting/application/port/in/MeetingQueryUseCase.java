package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.MeetingSort;
import org.springframework.data.domain.Page;

public interface MeetingQueryUseCase {

    Meeting getByMeetingId(Long meetingId, CustomUserDetails userDetails);

    Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, CustomUserDetails userDetails);
}
