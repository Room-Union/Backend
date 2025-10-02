package org.codeit.roomunion.moim.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum MoimRole {
    HOST("모임장"),
    MEMBER("크루원");

    private final String description;

    MoimRole(String description) {
        this.description = description;
    }

    @JsonCreator
    public static MoimRole from(String value) {
        for (MoimRole role : MoimRole.values()) {
            if (role.name().equalsIgnoreCase(value) || role.getDescription().equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("잘못된 MoimRole 값: " + value);
    }


}
