package org.codeit.roomunion.moim.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.UserNotFoundException;
import org.codeit.roomunion.moim.adapter.out.persistence.entity.CrewEntity;
import org.codeit.roomunion.moim.adapter.out.persistence.entity.CrewMemberEntity;
import org.codeit.roomunion.moim.adapter.out.persistence.jpa.CrewJpaRepository;
import org.codeit.roomunion.moim.adapter.out.persistence.jpa.CrewMemberJpaRepository;
import org.codeit.roomunion.moim.application.port.out.CrewRepository;
import org.codeit.roomunion.moim.domain.model.Crew;
import org.codeit.roomunion.moim.domain.model.command.CrewCreateCommand;
import org.codeit.roomunion.moim.domain.model.enums.CrewRole;
import org.codeit.roomunion.moim.exception.CrewErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

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
        Optional<CrewEntity> optional = crewJpaRepository.findById(crewId);
        if (optional.isEmpty()) return null;

        CrewEntity entity = optional.get();

        // HOST 유저 찾기
        Long hostUserId = crewMemberJpaRepository
            .findByCrewIdAndCrewRole(crewId, CrewRole.HOST)
            .map(crewMemberEntity -> crewMemberEntity.getUser().getId())
            .orElse(null);

        return entity.toDomain(hostUserId);

    }

    @Override
    public boolean existsCrewNameForHost(Long userId, String name) {
        return crewMemberJpaRepository.existsHostCrewName(userId, name, CrewRole.HOST);
    }
}
