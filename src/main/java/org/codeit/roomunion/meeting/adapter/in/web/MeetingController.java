package org.codeit.roomunion.meeting.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.adapter.in.web.request.CreateMeetingRequest;
import org.codeit.roomunion.meeting.adapter.in.web.response.MeetingResponse;
import org.codeit.roomunion.meeting.application.port.in.MeetingCommandUseCase;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/meeting")
public class MeetingController {

    private final MeetingCommandUseCase meetingCommandUseCase;
    private final UserQueryUseCase userQueryUseCase;

    @Operation(summary = "모임 생성", description = "모임 생성 버튼을 눌렀을때 요청되는 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MeetingResponse> createMeeting(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestPart("request") @Valid CreateMeetingRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        String email = userDetails.getUsername();
        MeetingCreateCommand command = request.toCommand(userDetails.getId());
        Meeting meeting = meetingCommandUseCase.create(command, image);
        User host = userQueryUseCase.getByEmail(email);
        return ResponseEntity.ok(MeetingResponse.from(meeting, host, true));
    }
}
