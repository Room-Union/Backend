package org.codeit.roomunion.moim.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum CrewRole {
    HOST("모임장"),
    MEMBER("크루원");

    private final String description;

    CrewRole(String description) {
        this.description = description;
    }

    @JsonCreator
    public static CrewRole from(String value) {
        for (CrewRole role : CrewRole.values()) {
            if (role.name().equalsIgnoreCase(value) || role.getDescription().equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("잘못된 CrewRole 값: " + value);
    }


}
