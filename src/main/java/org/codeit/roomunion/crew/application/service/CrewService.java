package org.codeit.roomunion.crew.application.service;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.crew.application.port.in.CrewCommandUseCase;
import org.codeit.roomunion.crew.application.port.in.CrewQueryUseCase;
import org.codeit.roomunion.crew.application.port.out.CrewRepository;
import org.codeit.roomunion.crew.domain.model.Crew;
import org.codeit.roomunion.crew.domain.model.command.CrewCreateCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewService implements CrewCommandUseCase, CrewQueryUseCase {

    private final CrewRepository crewRepository;

    @Override
    public Crew create(CrewCreateCommand command, MultipartFile image) {
        // 추후 예외는 ErrorCode로 변경 예정
        if (command.getMaxMemberCount() < 1) {
            throw new IllegalArgumentException("최대 참여자수는 1 이상이어야 합니다.");
        }

        Crew saved = crewRepository.createCrew(command, image);

        crewRepository.saveCrewMemberAsHost(saved.getId(), command.getUserId());

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Crew getByCrewId(Long crewId) {
        Crew crew = crewRepository.findById(crewId);
        // 추후 예외는 ErrorCode로 변경 예정
        if (crew == null) {
            throw new IllegalArgumentException("존재하지 않는 모임입니다. crewId=" + crewId);
        }
        return crew;
    }
}
