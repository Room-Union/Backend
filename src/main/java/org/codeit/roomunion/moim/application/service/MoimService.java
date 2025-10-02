package org.codeit.roomunion.moim.application.service;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.adapter.out.persistence.jpa.UuidJpaRepository;
import org.codeit.roomunion.common.adapter.out.s3.AmazonS3Manager;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.moim.application.port.in.MoimCommandUseCase;
import org.codeit.roomunion.moim.application.port.in.MoimQueryUseCase;
import org.codeit.roomunion.moim.application.port.out.MoimRepository;
import org.codeit.roomunion.moim.domain.model.Moim;
import org.codeit.roomunion.moim.domain.model.command.MoimCreateCommand;
import org.codeit.roomunion.moim.exception.MoimErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MoimService implements MoimCommandUseCase, MoimQueryUseCase {

    private final MoimRepository moimRepository;
    private final AmazonS3Manager s3Manager;
    private final UuidJpaRepository uuidJpaRepository;

    @Override
    public Moim create(MoimCreateCommand command, MultipartFile image) {
        // 추후 예외는 ErrorCode로 변경 예정
        if (command.getMaxMemberCount() < 1) {
            throw new CustomException(MoimErrorCode.INVALID_MAX_MEMBER_COUNT);
        }

        String normalizedName = command.getName().trim();
        if (moimRepository.existsMoimNameForHost(command.getUserId(), normalizedName)) {
            throw new CustomException(MoimErrorCode.DUPLICATE_MOIM_NAME);
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            UuidEntity savedUuid = uuidJpaRepository.save(
                UuidEntity.builder().uuid(uuid).build()
            );
            imageUrl = s3Manager.uploadFile(s3Manager.moimImageKey(savedUuid), image);
        }

        MoimCreateCommand finalCommand = MoimCreateCommand.builder()
            .name(command.getName())
            .description(command.getDescription())
            .category(command.getCategory())
            .maxMemberCount(command.getMaxMemberCount())
            .userId(command.getUserId())
            .platformURL(command.getPlatformURL())
            .imageUrl(imageUrl)
            .createdAt(LocalDateTime.now())
            .build();

        Moim saved = moimRepository.createMoim(finalCommand);

        moimRepository.saveMoimMemberAsHost(saved.getId(), command.getUserId());

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Moim getBymoimId(Long moimId) {
        Moim moim = moimRepository.findById(moimId);
        // 추후 예외는 ErrorCode로 변경 예정
        if (moim == null) {
            throw new CustomException(MoimErrorCode.MOIM_NOT_FOUND);
        }
        return moim;
    }
}
