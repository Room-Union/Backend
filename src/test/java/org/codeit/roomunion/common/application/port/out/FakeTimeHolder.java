package org.codeit.roomunion.common.application.port.out;

import java.time.LocalDateTime;

public class FakeTimeHolder implements TimeHolder {

    private final LocalDateTime localDateTime;

    public FakeTimeHolder(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public LocalDateTime localDateTime() {
        return localDateTime;
    }

}