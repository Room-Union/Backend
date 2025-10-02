package org.codeit.roomunion.moim.application.port.in;

import org.codeit.roomunion.moim.domain.model.Moim;
import org.codeit.roomunion.moim.domain.model.command.MoimCreateCommand;
import org.springframework.web.multipart.MultipartFile;

public interface MoimCommandUseCase {
    Moim create(MoimCreateCommand command, MultipartFile image);
}
