package org.codeit.roomunion.crew.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.crew.adapter.in.web.request.CreateCrewRequest;
import org.codeit.roomunion.crew.adapter.in.web.response.CrewResponse;
import org.codeit.roomunion.crew.application.port.in.CrewCommandUseCase;
import org.codeit.roomunion.crew.domain.model.Crew;
import org.codeit.roomunion.crew.domain.model.command.CrewCreateCommand;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/crew")
public class CrewController {

    private final CrewCommandUseCase crewCommandUseCase;
    private final UserQueryUseCase userQueryUseCase;

    @Operation(summary = "모임 생성", description = "모임 생성 버튼을 눌렀을때 요청되는 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CrewResponse> createCrew(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestPart("request") @Valid CreateCrewRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        String email = userDetails.getUsername();
        CrewCreateCommand command = request.toCommand(userDetails.getId());
        Crew crew = crewCommandUseCase.create(command, image);
        User host = userQueryUseCase.getByEmail(email);
        ResponseEntity.ok(CrewResponse.from(crew, host, true));

        return null;

    }
}
