package org.codeit.roomunion.meeting.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.adapter.in.web.request.ReadNotificationRequest;
import org.codeit.roomunion.meeting.adapter.in.web.response.NotificationResponse;
import org.codeit.roomunion.meeting.application.port.in.notification.NotificationUseCase;
import org.codeit.roomunion.meeting.domain.command.notification.CreateAndSendNotificationCommand;
import org.codeit.roomunion.meeting.domain.command.notification.SubscribeNotificationCommand;
import org.codeit.roomunion.meeting.domain.model.NotificationType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notification")
@Tag(name = "알림 API", description = "알림 관련 API")
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    @Operation(summary = "SSE 연결을 먼저 맺어야 알림을 실시간 수신합니다.(이거 제일 먼저 하기)", description = "토큰 필수, sse연결시 이거 제일 먼저 요청해서 연결 해놔야함")
    @GetMapping(value = "/sse/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return notificationUseCase.subscribe(SubscribeNotificationCommand.of(userDetails.getUser().getId()));
    }

    @Operation(summary = "(실제사용X)테스트용 sse로 메시지(내용) 보내기", description = "테스트용으로 알림 메시지 보내기")
    @PostMapping("/sse/publish")
    public void publish(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String content) {
        notificationUseCase.createAndSend(
            CreateAndSendNotificationCommand.of(
                userDetails.getUser().getId(),
                NotificationType.TEST,
                content,
                null,
                null
            ));
    }

    @Operation(
        summary = "알림 읽음 처리(단건/여러건)",
        description =
            "ids : 읽음 처리할 알림 id들<br/>" +
                "- 단건도 가능하며, 비어있거나 null이면 전체 읽음 처리됩니다.<br/><br/>" +
                "readAt : 알림을 읽은 시간<br/>" +
                "- 요청이 비어있으면 현재 시간으로 처리됩니다."
    )
    @PostMapping("/read")
    public void readNotifications(@AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody ReadNotificationRequest request) {
        notificationUseCase.read(request.toCommand(userDetails.getUser().getId()));
    }

    @Operation(summary = "(실제사용X)테스트용으로 읽지 않은 알림들 조회", description = "단순 읽지 않은 알림들 조회 기능")
    @GetMapping("/test-read")
    public ResponseEntity<List<NotificationResponse>> getUnReadNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<NotificationResponse> unreadNotifications = notificationUseCase.getUnread(userDetails.getUser().getId()).stream()
            .map(NotificationResponse::from)
            .toList();
        return ResponseEntity.ok(unreadNotifications);
    }

}
