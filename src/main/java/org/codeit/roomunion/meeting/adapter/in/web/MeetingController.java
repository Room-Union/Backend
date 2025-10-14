package org.codeit.roomunion.meeting.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.adapter.in.web.request.CreateMeetingRequest;
import org.codeit.roomunion.meeting.adapter.in.web.response.MeetingResponse;
import org.codeit.roomunion.meeting.application.port.in.MeetingCommandUseCase;
import org.codeit.roomunion.meeting.application.port.in.MeetingQueryUseCase;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingSort;
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
        MeetingCreateCommand command = request.toCommand(userDetails.getId(), userDetails.getUsername());
        Meeting meeting = meetingCommandUseCase.create(command, image);
        return ResponseEntity.ok(MeetingResponse.from(meeting));
    }

    @Operation(summary = "특정 모임 조회(토큰 없이도 가능)", description = "meetingId로 모임 상세 정보를 조회. 로그인 시 isJoined 계산, 비로그인은 false")
    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingResponse> getMeeting(
        @AuthenticationPrincipal CustomUserDetails userDetails, // null 허용
        @PathVariable Long meetingId
    ) {
        Long userId = (userDetails != null) ? userDetails.getId() : null;
        Meeting meeting = meetingQueryUseCase.getByMeetingId(meetingId, userId);
        return ResponseEntity.ok(MeetingResponse.from(meeting));
    }

    @Operation(summary = "전체/카테고리 모임 리스트 조회(토큰 없이도 가능)", description = "전체/카테고리별 조회 + 정렬(최신순/사람많은 순) + 페이징처리. 로그인 시 isJoined 계산, 비로그인은 false")
    @GetMapping
    public ResponseEntity<Page<MeetingResponse>> getMeetingList(
        @AuthenticationPrincipal CustomUserDetails userDetails, // null 허용
        @RequestParam(required = false) MeetingCategory category,
        @RequestParam(defaultValue = "LATEST") MeetingSort sort,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = (userDetails != null) ? userDetails.getId() : null;
        Page<Meeting> meetings = meetingQueryUseCase.search(category, sort, page, size, userId);
        Page<MeetingResponse> response = meetings.map(MeetingResponse::from);
        return ResponseEntity.ok(response);
    }


}
