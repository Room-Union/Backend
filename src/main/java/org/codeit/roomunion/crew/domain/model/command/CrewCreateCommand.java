package org.codeit.roomunion.crew.domain.model.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CrewCreateCommand {

    private final String name;

    private final String description;

//    private final Category category;

    private final int maxMemberCount;

    private final List<String> platformURL;

    private final Long userId;


}
