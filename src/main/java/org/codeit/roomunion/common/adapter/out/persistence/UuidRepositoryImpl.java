package org.codeit.roomunion.common.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.adapter.out.persistence.jpa.UuidJpaRepository;
import org.codeit.roomunion.common.application.port.out.UuidRepository;
import org.codeit.roomunion.common.domain.model.Uuid;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UuidRepositoryImpl implements UuidRepository {

    private final UuidJpaRepository uuidJpaRepository;

    @Override
    public Uuid save(Uuid uuid) {
        UuidEntity saved = uuidJpaRepository.save(UuidEntity.from(uuid));
        return saved.toDomain();
    }
}
