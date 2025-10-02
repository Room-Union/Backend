package org.codeit.roomunion.moim.application.port.in;

import org.codeit.roomunion.moim.domain.model.Crew;

public interface CrewQueryUseCase {

    Crew getByCrewId(Long crewId);
}
