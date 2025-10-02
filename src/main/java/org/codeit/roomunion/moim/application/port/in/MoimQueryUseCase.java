package org.codeit.roomunion.moim.application.port.in;

import org.codeit.roomunion.moim.domain.model.Moim;

public interface MoimQueryUseCase {

    Moim getBymoimId(Long moimId);
}
