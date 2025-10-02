package org.codeit.roomunion.moim.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.moim.adapter.in.web.request.CreateMoimRequest;
import org.codeit.roomunion.moim.adapter.in.web.response.MoimResponse;
import org.codeit.roomunion.moim.application.port.in.MoimCommandUseCase;
import org.codeit.roomunion.moim.domain.model.Moim;
import org.codeit.roomunion.moim.domain.model.command.MoimCreateCommand;
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
@RequestMapping("/v1/moim")
public class MoimController {

    private final MoimCommandUseCase moimCommandUseCase;
    private final UserQueryUseCase userQueryUseCase;

    @Operation(summary = "모임 생성", description = "모임 생성 버튼을 눌렀을때 요청되는 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MoimResponse> createMoim(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestPart("request") @Valid CreateMoimRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        String email = userDetails.getUsername();
        MoimCreateCommand command = request.toCommand(userDetails.getId());
        Moim moim = moimCommandUseCase.create(command, image);
        User host = userQueryUseCase.getByEmail(email);
        return ResponseEntity.ok(MoimResponse.from(moim, host, true));
    }
}
