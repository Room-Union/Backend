package org.codeit.roomunion.meeting.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum MeetingRole {
    HOST("모임장"),
    MEMBER("크루원");

    private final String description;

    MeetingRole(String description) {
        this.description = description;
    }

    @JsonCreator
    public static MeetingRole from(String value) {
        for (MeetingRole role : MeetingRole.values()) {
            if (role.name().equalsIgnoreCase(value) || role.getDescription().equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("잘못된 MeetingRole 값: " + value);
    }


}
