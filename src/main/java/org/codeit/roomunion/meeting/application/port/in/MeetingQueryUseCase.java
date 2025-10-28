package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.MeetingRole;
import org.codeit.roomunion.meeting.domain.model.MeetingSort;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.data.domain.Page;

public interface MeetingQueryUseCase {

    Meeting getByMeetingId(Long meetingId, CustomUserDetails userDetails);

    Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, CustomUserDetails userDetails);

    Page<Meeting> getMyMeetings(MeetingRole role, int page, int size, CustomUserDetails userDetails);

    boolean existsMemberBy(Long meetingId, User user);

    Page<Meeting> searchByName(String keyword, int page, int size, CustomUserDetails userDetails);
}
