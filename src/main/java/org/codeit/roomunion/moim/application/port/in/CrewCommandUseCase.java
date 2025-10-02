package org.codeit.roomunion.moim.application.port.in;

import org.codeit.roomunion.moim.domain.model.Crew;
import org.codeit.roomunion.moim.domain.model.command.CrewCreateCommand;
import org.springframework.web.multipart.MultipartFile;

public interface CrewCommandUseCase {
    Crew create(CrewCreateCommand command, MultipartFile image);
}
