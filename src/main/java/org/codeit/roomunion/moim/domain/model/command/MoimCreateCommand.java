package org.codeit.roomunion.moim.domain.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.codeit.roomunion.moim.domain.model.enums.MoimCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MoimCreateCommand {

    private final String name;

    private final String description;

    private final MoimCategory category;

    private final int maxMemberCount;

    private final List<String> platformURL;

    private final Long userId;

    private final String imageUrl;

    private final LocalDateTime createdAt;


}
