package org.codeit.roomunion.meeting.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.adapter.in.web.request.CreateMeetingRequest;
import org.codeit.roomunion.meeting.adapter.in.web.response.MeetingResponse;
import org.codeit.roomunion.meeting.application.port.in.MeetingCommandUseCase;
import org.codeit.roomunion.meeting.application.port.in.MeetingQueryUseCase;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final MeetingQueryUseCase meetingQueryUseCase;

    @Operation(summary = "모임 생성", description = "모임 생성 버튼을 눌렀을때 요청되는 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MeetingResponse> createMeeting(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestPart("request") @Valid CreateMeetingRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        MeetingCreateCommand command = request.toCommand(userDetails.getId(), userDetails.getUsername());
        Meeting meeting = meetingCommandUseCase.create(command, image);
        return ResponseEntity.ok(MeetingResponse.from(meeting, true));
    }

    @Operation(summary = "특정 모임 조회", description = "meetingId로 모임 상세 정보를 조회. isJoined 필드로 인해 토큰 필수")
    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingResponse> getMeeting(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long meetingId
    ) {
        Meeting meeting = meetingQueryUseCase.getByMeetingId(meetingId, userDetails.getId());
        return ResponseEntity.ok(MeetingResponse.from(meeting, meeting.isJoined()));
    }

}
