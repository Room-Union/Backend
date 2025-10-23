package org.codeit.roomunion.meeting.domain.policy;

import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.GlobalErrorCode;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.domain.model.User;

import java.time.LocalDateTime;

public class AppointmentPolicy {
    private AppointmentPolicy() {
    }

    public static void validateIsHost(Meeting meeting, User user) {
        if (meeting.isNotHost(user.getId())) {
            throw new CustomException(MeetingErrorCode.APPOINTMENT_CUD_FORBIDDEN);
        }
    }

    public static void validate(AppointmentCreateCommand command, LocalDateTime currentAt) {
        validateNonNull(command, command.getMeetingId(), command.getTitle(), command.getMaxMemberCount(), command.getScheduledAt());
        if (validateTitle(command.getTitle())) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
        if (validateMaxMemberCount(command.getMaxMemberCount())) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
        if (validateScheduledAt(command.getScheduledAt(), currentAt)) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private static boolean validateScheduledAt(LocalDateTime scheduledAt, LocalDateTime currentAt) {
        return scheduledAt.isBefore(currentAt);
    }

    private static boolean validateMaxMemberCount(int maxMemberCount) {
        return maxMemberCount < 2 || maxMemberCount > 50;
    }

    private static boolean validateTitle(String title) {
        return title.isBlank() || title.length() > 50;
    }

    private static void validateNonNull(Object... objects) {
        for (Object o : objects) {
            validateIsNotNull(o);
        }
    }

    private static void validateIsNotNull(Object object) {
        if (object == null) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
