package org.codeit.roomunion.crew.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.adapter.out.persistence.jpa.UuidJpaRepository;
import org.codeit.roomunion.common.adapter.out.s3.AmazonS3Manager;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.UserNotFoundException;
import org.codeit.roomunion.crew.adapter.out.persistence.entity.CrewEntity;
import org.codeit.roomunion.crew.adapter.out.persistence.entity.CrewMemberEntity;
import org.codeit.roomunion.crew.adapter.out.persistence.jpa.CrewJpaRepository;
import org.codeit.roomunion.crew.adapter.out.persistence.jpa.CrewMemberJpaRepository;
import org.codeit.roomunion.crew.application.port.out.CrewRepository;
import org.codeit.roomunion.crew.domain.model.Crew;
import org.codeit.roomunion.crew.domain.model.command.CrewCreateCommand;
import org.codeit.roomunion.crew.domain.model.enums.CrewRole;
import org.codeit.roomunion.crew.exception.CrewErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CrewRepositoryImpl implements CrewRepository {

    private final CrewJpaRepository crewJpaRepository;
    private final CrewMemberJpaRepository crewMemberJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Crew createCrew(CrewCreateCommand command) {

        CrewEntity crewEntity = CrewEntity.builder()
            .name(command.getName())
            .description(command.getDescription())
            .category(command.getCategory())
            .crewImage(command.getImageUrl())
            .maxMemberCount(command.getMaxMemberCount())
            .platformUrls(command.getPlatformURL())
            .createdAt(LocalDateTime.now())
            .build();

        CrewEntity savedCrewEntity = crewJpaRepository.save(crewEntity);

        return savedCrewEntity.toDomain(command.getUserId());
    }

    @Override
    public void saveCrewMemberAsHost(Long crewId, Long userId) {
        CrewEntity crew = crewJpaRepository.findById(crewId)
            .orElseThrow(() -> new CustomException(CrewErrorCode.CREW_NOT_FOUND));

        // TODO 현태님 UserErrorCode로 변경하시면 예외처리 적용
        UserEntity user = userJpaRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        CrewMemberEntity crewMember = CrewMemberEntity.builder()
            .crew(crew)
            .user(user)
            .crewRole(CrewRole.HOST)
            .build();

        crewMemberJpaRepository.save(crewMember);

    }

    @Override
    public Crew findById(Long crewId) {
        Optional<CrewEntity> optional  = crewJpaRepository.findById(crewId);
        if (optional .isEmpty()) return null;

        CrewEntity entity = optional.get();

        // HOST 유저 찾기
        Long hostUserId = crewMemberJpaRepository
            .findByCrewIdAndCrewRole(crewId, CrewRole.HOST)
            .map(crewMemberEntity ->  crewMemberEntity.getUser().getId())
            .orElse(null);

        return entity.toDomain(hostUserId);

    }

    @Override
    public boolean existsCrewNameForHost(Long userId, String name) {
        return crewMemberJpaRepository.existsHostCrewName(userId, name, CrewRole.HOST);
    }
}
