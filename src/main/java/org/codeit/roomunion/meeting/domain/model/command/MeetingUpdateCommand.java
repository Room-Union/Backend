package org.codeit.roomunion.meeting.domain.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MeetingUpdateCommand {

    private final String name;
    private final String description;
    private final MeetingCategory category;
    private final Integer maxMemberCount;
    private final List<String> platformURL;
    private final String imageUrl;

    public static MeetingUpdateCommand of(MeetingUpdateCommand baseCommand, String imageUrl) {
        return MeetingUpdateCommand.builder()
            .name(baseCommand.getName())
            .description(baseCommand.getDescription())
            .category(baseCommand.getCategory())
            .maxMemberCount(baseCommand.getMaxMemberCount())
            .platformURL(baseCommand.getPlatformURL())
            .imageUrl(imageUrl)
            .build();
    }


}