package org.codeit.roomunion.moim.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.UserNotFoundException;
import org.codeit.roomunion.moim.adapter.out.persistence.entity.MoimEntity;
import org.codeit.roomunion.moim.adapter.out.persistence.entity.MoimMemberEntity;
import org.codeit.roomunion.moim.adapter.out.persistence.jpa.MoimJpaRepository;
import org.codeit.roomunion.moim.adapter.out.persistence.jpa.MoimMemberJpaRepository;
import org.codeit.roomunion.moim.application.port.out.MoimRepository;
import org.codeit.roomunion.moim.domain.model.Moim;
import org.codeit.roomunion.moim.domain.model.command.MoimCreateCommand;
import org.codeit.roomunion.moim.domain.model.enums.MoimRole;
import org.codeit.roomunion.moim.exception.MoimErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MoimRepositoryImpl implements MoimRepository {

    private final MoimJpaRepository moimJpaRepository;
    private final MoimMemberJpaRepository moimMemberJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Moim createMoim(MoimCreateCommand command) {

        MoimEntity moimEntity = MoimEntity.builder()
            .name(command.getName())
            .description(command.getDescription())
            .category(command.getCategory())
            .moimImage(command.getImageUrl())
            .maxMemberCount(command.getMaxMemberCount())
            .platformUrls(command.getPlatformURL())
            .createdAt(LocalDateTime.now())
            .build();

        MoimEntity savedMoimEntity = moimJpaRepository.save(moimEntity);

        return savedMoimEntity.toDomain(command.getUserId());
    }

    @Override
    public void saveMoimMemberAsHost(Long moimId, Long userId) {
        MoimEntity moim = moimJpaRepository.findById(moimId)
            .orElseThrow(() -> new CustomException(MoimErrorCode.MOIM_NOT_FOUND));

        // TODO 현태님 UserErrorCode로 변경하시면 예외처리 적용
        UserEntity user = userJpaRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        MoimMemberEntity moimMember = MoimMemberEntity.builder()
            .moim(moim)
            .user(user)
            .moimRole(MoimRole.HOST)
            .build();

        moimMemberJpaRepository.save(moimMember);

    }

    @Override
    public Moim findById(Long moimId) {
        Optional<MoimEntity> optional = moimJpaRepository.findById(moimId);
        if (optional.isEmpty()) return null;

        MoimEntity entity = optional.get();

        // HOST 유저 찾기
        Long hostUserId = moimMemberJpaRepository
            .findByMoimIdAndgetMoimRole(moimId, MoimRole.HOST)
            .map(moimMemberEntity -> moimMemberEntity.getUser().getId())
            .orElse(null);

        return entity.toDomain(hostUserId);

    }

    @Override
    public boolean existsMoimNameForHost(Long userId, String name) {
        return moimMemberJpaRepository.existsMoimByHostAndName(userId, name, MoimRole.HOST);
    }
}
