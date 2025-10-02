package org.codeit.roomunion.meeting.domain.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MeetingCreateCommand {

    private final String name;

    private final String description;

    private final MeetingCategory category;

    private final int maxMemberCount;

    private final List<String> platformURL;

    private final Long userId;

    private final String hostEmail;

    private final String imageUrl;

    private final LocalDateTime createdAt;


}
