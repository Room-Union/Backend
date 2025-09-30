package org.codeit.roomunion.crew.application.port.in;

import org.codeit.roomunion.crew.domain.model.Crew;
import org.codeit.roomunion.crew.domain.model.command.CrewCreateCommand;
import org.springframework.web.multipart.MultipartFile;

public interface CrewCommandUseCase {
    Crew create(CrewCreateCommand command, MultipartFile image);
}
