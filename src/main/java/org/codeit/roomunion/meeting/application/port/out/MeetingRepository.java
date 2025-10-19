package org.codeit.roomunion.meeting.application.port.out;

import org.codeit.roomunion.meeting.domain.command.MeetingUpdateCommand;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.MeetingRole;
import org.codeit.roomunion.meeting.domain.model.MeetingSort;
import org.springframework.data.domain.Page;

public interface MeetingRepository {
    Meeting createMeeting(MeetingCreateCommand command);

    Meeting findById(Long meetingId);

    boolean existsMeetingNameForHost(Long userId, String name);

    boolean existsMeetingById(Long meetingId);

    int countJoinedMembers(Long meetingId);

    Page<Meeting> search(MeetingCategory category, MeetingSort sort, int page, int size, Long currentUserId);

    Meeting findByIdWithJoined(Long meetingId, Long currentUserId);

    boolean isMeetingMember(Long meetingId, Long userId);

    boolean isHostMember(Long meetingId, Long userId);

    void insertMember(Long meetingId, Long userId, MeetingRole role);

    void updateMeeting(Long meetingId, MeetingUpdateCommand command);

    void deleteMeeting(Long meetingId);



}
