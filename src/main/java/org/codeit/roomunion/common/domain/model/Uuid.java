package org.codeit.roomunion.common.domain.model;

import lombok.Getter;

@Getter
public class Uuid {

    private final String value;

    public Uuid(String value) {
        this.value = value;
    }

    public static Uuid of(String value) {
        return new Uuid(value);
    }
}
