package org.codeit.roomunion.meeting.domain.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;

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
    private final String removeImageUrl;

    public static MeetingUpdateCommand of(MeetingUpdateCommand baseCommand, String imageUrl) {
        return MeetingUpdateCommand.builder()
            .name(baseCommand.getName())
            .description(baseCommand.getDescription())
            .category(baseCommand.getCategory())
            .maxMemberCount(baseCommand.getMaxMemberCount())
            .platformURL(baseCommand.getPlatformURL())
            .imageUrl(imageUrl)
            .removeImageUrl(baseCommand.getRemoveImageUrl())
            .build();
    }


}