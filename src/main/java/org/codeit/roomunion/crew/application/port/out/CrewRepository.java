package org.codeit.roomunion.crew.application.port.out;

import org.codeit.roomunion.crew.domain.model.Crew;
import org.codeit.roomunion.crew.domain.model.command.CrewCreateCommand;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

public interface CrewRepository {
    Crew createCrew(CrewCreateCommand command);

    void saveCrewMemberAsHost(Long crewId, Long userId);

    Crew findById(Long crewId);

    boolean existsCrewNameForHost(Long userId, String name); // 해당 사용자가 같은 이름의 모임을 이미 보유하고 있는지

}
