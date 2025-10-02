package org.codeit.roomunion.meeting.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codeit.roomunion.common.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MeetingErrorCode implements BaseErrorCode {

    INVALID_MAX_MEMBER_COUNT(4001, "최대 참여자수는 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_MEETING_NAME(4002, "이미 동일한 이름의 모임을 보유하고 있습니다.", HttpStatus.BAD_REQUEST),
    MEETING_NOT_FOUND(4003, "존재하지 않는 모임입니다.", HttpStatus.NOT_FOUND),
    MEETING_MEMBER_NOT_FOUND(4004, "존재하지 않는 크루원입니다.", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
