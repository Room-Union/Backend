package org.codeit.roomunion.meeting.application.port.in.notification;

import java.util.List;
import org.codeit.roomunion.meeting.domain.command.notification.CreateAndSendNotificationCommand;
import org.codeit.roomunion.meeting.domain.command.notification.ReadNotificationCommand;
import org.codeit.roomunion.meeting.domain.command.notification.SubscribeNotificationCommand;
import org.codeit.roomunion.meeting.domain.model.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationUseCase {
    SseEmitter subscribe(SubscribeNotificationCommand command);

    void createAndSend(CreateAndSendNotificationCommand command);

    void read(ReadNotificationCommand command);

    List<Notification> getUnread(Long userId);
}
