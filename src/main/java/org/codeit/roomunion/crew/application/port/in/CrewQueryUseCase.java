package org.codeit.roomunion.crew.application.port.in;

import org.codeit.roomunion.crew.domain.model.Crew;

public interface CrewQueryUseCase {

    Crew getByCrewId(Long crewId);
}
