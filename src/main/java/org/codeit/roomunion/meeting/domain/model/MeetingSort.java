package org.codeit.roomunion.meeting.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum MeetingSort {
    LATEST("최신순"),
    MEMBER_DESC("인원 많은 순");

    private final String description;

    MeetingSort(String description) {
        this.description = description;
    }

    @JsonCreator
    public static MeetingSort from(String value) {
        for (MeetingSort sort: values()) {
            if (sort.name().equalsIgnoreCase(value)) {
                return sort;
            }
        }
        throw new IllegalArgumentException("잘못된 MeetingSort 값: " + value);
    }
}
