package org.codeit.roomunion.meeting.domain.model;

public enum NotificationType {

    //TEST
    TEST,

    // 약속 시작 알림
    APPOINTMENT_START,

    // 약속 생성 알림
    APPOINTMENT_CREATED,

    // 약속 취소 알림
    APPOINTMENT_CANCELED,

    // 모임 삭제 알림
    MEETING_DELETED,

    // 약속 인원 증원 알림
    APPOINTMENT_CAPACITY_INCREASED
}
