package org.codeit.roomunion.meeting.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.adapter.in.web.request.CreateAppointmentRequest;
import org.codeit.roomunion.meeting.adapter.in.web.request.ModifyAppointmentRequest;
import org.codeit.roomunion.meeting.adapter.in.web.response.AppointmentsResponse;
import org.codeit.roomunion.meeting.application.port.in.AppointmentCommandUseCase;
import org.codeit.roomunion.meeting.application.port.in.AppointmentQueryUseCase;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.codeit.roomunion.meeting.domain.command.AppointmentModifyCommand;
import org.codeit.roomunion.meeting.domain.model.Appointment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/meetings")
@Tag(name = "모임 약속 API", description = "모임 약속 전용 API")
public class AppointmentController {

    private final AppointmentCommandUseCase appointmentCommandUseCase;
    private final AppointmentQueryUseCase appointmentQueryUseCase;

    public AppointmentController(AppointmentCommandUseCase appointmentCommandUseCase, AppointmentQueryUseCase appointmentQueryUseCase) {
        this.appointmentCommandUseCase = appointmentCommandUseCase;
        this.appointmentQueryUseCase = appointmentQueryUseCase;
    }

    @GetMapping("/{meetingId}/appointments")
    public ResponseEntity<AppointmentsResponse> getAppointments(
        @PathVariable Long meetingId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<Appointment> appointments = appointmentQueryUseCase.getAppointments(meetingId, customUserDetails);
        return ResponseEntity.ok(AppointmentsResponse.from(appointments));
    }

    @PostMapping("/{meetingId}/appointments")
    public ResponseEntity<Void> createAppointment(
        @PathVariable Long meetingId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @ModelAttribute CreateAppointmentRequest request,
        @RequestPart(required = false) MultipartFile image
    ) {
        AppointmentCreateCommand command = request.toCommand(meetingId);
        appointmentCommandUseCase.create(customUserDetails, command, image);
        return ResponseEntity.noContent()
            .build();
    }

    @PutMapping("/{meetingId}/appointments/{appointmentId}")
    public ResponseEntity<Void> modifyAppointment(
        @PathVariable Long meetingId,
        @PathVariable Long appointmentId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @ModelAttribute ModifyAppointmentRequest request,
        @RequestPart(required = false) MultipartFile image
    ) {
        AppointmentModifyCommand command = request.toCommand(meetingId, appointmentId);
        appointmentCommandUseCase.modify(customUserDetails, command, image);
        return ResponseEntity.noContent()
            .build();
    }

    @DeleteMapping("/{meetingId}/appointments/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(
        @PathVariable Long meetingId,
        @PathVariable Long appointmentId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        appointmentCommandUseCase.delete(customUserDetails, meetingId, appointmentId);
        return ResponseEntity.noContent()
            .build();
    }

    @PostMapping("/{meetingId}/appointments/{appointmentId}/join")
    public ResponseEntity<Void> joinAppointment(
        @PathVariable Long meetingId,
        @PathVariable Long appointmentId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        appointmentCommandUseCase.join(customUserDetails, meetingId, appointmentId);
        return ResponseEntity.noContent()
            .build();
    }

    @DeleteMapping("/{meetingId}/appointments/{appointmentId}/leave")
    public ResponseEntity<Void> leaveAppointment(
        @PathVariable Long meetingId,
        @PathVariable Long appointmentId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        appointmentCommandUseCase.leave(customUserDetails, appointmentId);
        return ResponseEntity.noContent()
            .build();
    }
}
