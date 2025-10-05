package org.codeit.roomunion.common.application.port.out;

import org.codeit.roomunion.common.domain.model.Uuid;

public interface UuidRepository {
    Uuid save(Uuid uuid);
}
