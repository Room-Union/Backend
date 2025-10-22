package org.codeit.roomunion.meeting.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.adapter.in.web.request.CreateMeetingRequest;
import org.codeit.roomunion.meeting.adapter.in.web.request.UpdateMeetingRequest;
import org.codeit.roomunion.meeting.adapter.in.web.response.MeetingResponse;
import org.codeit.roomunion.meeting.application.port.in.MeetingCommandUseCase;
import org.codeit.roomunion.meeting.application.port.in.MeetingQueryUseCase;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.command.MeetingUpdateCommand;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.MeetingSort;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/meetings")
@Tag(name = "모임 API", description = "모임 전용 API")
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
        MeetingCreateCommand command = request.toCommand(userDetails.getUser());
        Meeting meeting = meetingCommandUseCase.create(command, image);
        return ResponseEntity.ok(MeetingResponse.from(meeting));
    }

    @Operation(summary = "특정 모임 조회(토큰 없이도 가능)", description = "meetingId로 모임 상세 정보를 조회. 로그인 시 isJoined 계산, 비로그인은 false")
    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingResponse> getMeeting(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long meetingId
    ) {
        Meeting meeting = meetingQueryUseCase.getByMeetingId(meetingId, userDetails);
        return ResponseEntity.ok(MeetingResponse.from(meeting));
    }

    @Operation(summary = "전체/카테고리 모임 리스트 조회(토큰 없이도 가능)", description = "전체/카테고리별 조회 + 정렬(최신순/사람많은 순) + 페이징처리. 로그인 시 isJoined 계산, 비로그인은 false")
    @GetMapping
    public ResponseEntity<Page<MeetingResponse>> getMeetingList(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(required = false) MeetingCategory category,
        @RequestParam(defaultValue = "LATEST") MeetingSort sort,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<Meeting> meetings = meetingQueryUseCase.search(category, sort, page, size, userDetails);
        Page<MeetingResponse> response = meetings.map(MeetingResponse::from);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 모임 가입하기", description = "meetingId로 특정 모임 가입 API. 중복 가입 X")
    @PostMapping("/{meetingId}/join")
    public ResponseEntity<MeetingResponse> join(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long meetingId
    ) {
        Meeting meeting = meetingCommandUseCase.join(meetingId, userDetails.getUser().getId());
        return ResponseEntity.ok(MeetingResponse.from(meeting));
    }

    @Operation(summary = "모임 수정", description = "모임장만 수정가능, 토큰 필수")
    @PutMapping(value = "/{meetingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MeetingResponse> updateMeeting(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long meetingId,
        @RequestPart("request") @Valid UpdateMeetingRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        MeetingUpdateCommand command = request.toCommand();
        Meeting updatedMeeting = meetingCommandUseCase.update(meetingId, userDetails.getUser().getId(), command, image);
        return ResponseEntity.ok(MeetingResponse.from(updatedMeeting));
    }

    @Operation(summary = "모임 삭제", description = "모임장만 삭제 가능, 토큰 필수")
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Map<String, String>> deleteMeeting(
        @PathVariable Long meetingId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        meetingCommandUseCase.deleteMeeting(meetingId, userDetails.getUser().getId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "모임이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }


}
