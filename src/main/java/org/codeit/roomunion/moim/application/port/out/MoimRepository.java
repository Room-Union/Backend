package org.codeit.roomunion.moim.application.port.out;

import org.codeit.roomunion.moim.domain.model.Moim;
import org.codeit.roomunion.moim.domain.model.command.MoimCreateCommand;

public interface MoimRepository {
    Moim createMoim(MoimCreateCommand command);

    void saveMoimMemberAsHost(Long moimId, Long userId);

    Moim findById(Long moimId);

    boolean existsMoimNameForHost(Long userId, String name); // 해당 사용자가 같은 이름의 모임을 이미 보유하고 있는지

}
