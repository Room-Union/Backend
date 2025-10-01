package org.codeit.roomunion.crew.application.service;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.adapter.out.persistence.jpa.UuidJpaRepository;
import org.codeit.roomunion.common.adapter.out.s3.AmazonS3Manager;
import org.codeit.roomunion.crew.adapter.out.persistence.entity.CrewEntity;
import org.codeit.roomunion.crew.adapter.out.persistence.entity.CrewMemberEntity;
import org.codeit.roomunion.crew.adapter.out.persistence.jpa.CrewJpaRepository;
import org.codeit.roomunion.crew.adapter.out.persistence.jpa.CrewMemberJpaRepository;
import org.codeit.roomunion.crew.application.port.in.CrewCommandUseCase;
import org.codeit.roomunion.crew.application.port.in.CrewQueryUseCase;
import org.codeit.roomunion.crew.application.port.out.CrewRepository;
import org.codeit.roomunion.crew.domain.model.Crew;
import org.codeit.roomunion.crew.domain.model.command.CrewCreateCommand;
import org.codeit.roomunion.crew.domain.model.enums.CrewRole;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CrewService implements CrewCommandUseCase, CrewQueryUseCase {

    private final CrewRepository crewRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidJpaRepository uuidJpaRepository;

    @Override
    public Crew create(CrewCreateCommand command, MultipartFile image) {
        // 추후 예외는 ErrorCode로 변경 예정
        if (command.getMaxMemberCount() < 1) {
            throw new IllegalArgumentException("최대 참여자수는 1 이상이어야 합니다.");
        }

        String normalizedName = command.getName().trim();
        if (crewRepository.existsCrewNameForHost(command.getUserId(), normalizedName)) {
            throw new IllegalStateException("이미 동일한 이름의 모임을 보유하고 있습니다: " + normalizedName);
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            UuidEntity savedUuid = uuidJpaRepository.save(
                UuidEntity.builder().uuid(uuid).build()
            );
            imageUrl = s3Manager.uploadFile(s3Manager.crewImageKey(savedUuid), image);
        }

        CrewCreateCommand finalCommand = CrewCreateCommand.builder()
            .name(command.getName())
            .description(command.getDescription())
            .category(command.getCategory())
            .maxMemberCount(command.getMaxMemberCount())
            .userId(command.getUserId())
            .platformURL(command.getPlatformURL())
            .imageUrl(imageUrl)
            .createdAt(LocalDateTime.now())
            .build();

        Crew saved = crewRepository.createCrew(finalCommand);

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
