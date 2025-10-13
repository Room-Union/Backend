package org.codeit.roomunion.common.adapter.out.system;

import org.codeit.roomunion.common.application.port.out.TimeHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeHolderImpl implements TimeHolder {

    @Override
    public LocalDateTime localDateTime() {
        return LocalDateTime.now();
    }

}
