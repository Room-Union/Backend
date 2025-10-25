package org.codeit.roomunion.meeting.adapter.in.web;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.adapter.in.web.request.CreateAppointmentRequest;
import org.codeit.roomunion.meeting.adapter.in.web.request.ModifyAppointmentRequest;
import org.codeit.roomunion.meeting.application.port.in.AppointmentCommandUseCase;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.codeit.roomunion.meeting.domain.command.AppointmentModifyCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/meetings")
public class AppointmentController {

    private final AppointmentCommandUseCase appointmentCommandUseCase;

    public AppointmentController(AppointmentCommandUseCase appointmentCommandUseCase) {
        this.appointmentCommandUseCase = appointmentCommandUseCase;
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
