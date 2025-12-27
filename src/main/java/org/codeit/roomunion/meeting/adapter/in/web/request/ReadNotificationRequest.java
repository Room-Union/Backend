package org.codeit.roomunion.meeting.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.command.notification.ReadNotificationCommand;

@Getter
@Schema(title = "ReadNotificationRequest : 알림 읽기 요청 DTO")
public class ReadNotificationRequest {

    private List<Long> ids;
    private LocalDateTime readAt;

    public ReadNotificationCommand toCommand(@NotNull Long userId) {
        return new ReadNotificationCommand(userId, ids, readAt);
    }

}
