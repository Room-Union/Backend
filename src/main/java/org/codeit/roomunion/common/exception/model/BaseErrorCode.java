package org.codeit.roomunion.common.exception.model;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    int getCode();

    String getMessage();

    HttpStatus getStatus();
}
