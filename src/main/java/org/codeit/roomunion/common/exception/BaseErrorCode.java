package org.codeit.roomunion.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    int getCode();

    String getMessage();

    HttpStatus getStatus();

    default int getStatusValue() {
        return getStatus().value();
    }
}
