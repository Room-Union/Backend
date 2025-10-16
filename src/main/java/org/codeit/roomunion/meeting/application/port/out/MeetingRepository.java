package org.codeit.roomunion.meeting.application.port.out;

import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingRole;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingSort;
import org.springframework.data.domain.Page;

public interface MeetingRepository {
    Meeting createMeeting(MeetingCreateCommand command);

    Meeting findById(Long meetingId);

    boolean existsMeetingNameForHost(Long userId, String name); // 해당 사용자가 같은 이름의 모임을 이미 보유하고 있는지

    int countJoinedMembers(Long meetingId);

    Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, Long currentUserId);

    Meeting findByIdWithJoined(Long meetingId, Long currentUserId);

    boolean isMeetingMember(Long meetingId, Long userId);

    void insertMember(Long meetingId, Long userId, MeetingRole role);

}
