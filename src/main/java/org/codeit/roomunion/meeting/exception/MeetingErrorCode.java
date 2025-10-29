package org.codeit.roomunion.meeting.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codeit.roomunion.common.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MeetingErrorCode implements BaseErrorCode {

    INVALID_MAX_MEMBER_COUNT("최대 참여자수는 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_MEETING_NAME("이미 동일한 이름의 모임을 보유하고 있습니다.", HttpStatus.BAD_REQUEST),
    MEETING_NOT_FOUND("존재하지 않는 모임입니다.", HttpStatus.NOT_FOUND),
    MEETING_MEMBER_NOT_FOUND("존재하지 않는 모임원입니다.", HttpStatus.NOT_FOUND),
    MEETING_HOST_NOT_FOUND("모임장이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MEETING_MEMBER_LIMIT_REACHED("모임 최대 멤버수에 도달하였습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_JOINED("이미 가입한 모임입니다.", HttpStatus.BAD_REQUEST),
    NOT_JOINED("가입한 모임이 아닙니다.", HttpStatus.BAD_REQUEST),

    MEETING_HOST_CANNOT_LEAVE("모임장은 탈퇴할 수 없습니다.", HttpStatus.FORBIDDEN),
    MEETING_MODIFY_FORBIDDEN("모임장만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
    MEETING_DELETE_FORBIDDEN("모임장만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    MEETING_MODIFY_INVALID_REQUEST("모임 수정 요청에 빈 값이 존재합니다.", HttpStatus.BAD_REQUEST),
    MAX_COUNT_LESS_THAN_CURRENT("현재 참여자 수보다 작은 최대 인원으로 수정할 수 없습니다.", HttpStatus.BAD_REQUEST),

    APPOINTMENT_CUD_FORBIDDEN("모임 일정은 모임장만 생성/변경할 수 있습니다.", HttpStatus.FORBIDDEN),
    APPOINTMENT_NOT_FOUND("약속을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    APPOINTMENT_ALREADY_JOINED("이미 가입한 약속입니다.", HttpStatus.BAD_REQUEST),
    APPOINTMENT_NOT_JOINED("가입한 약속이 아닙니다.", HttpStatus.BAD_REQUEST),
    APPOINTMENT_MEMBER_LIMIT_REACHED("약속 최대 멤버수에 도달하였습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus status;

}
