package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;

public interface UserCommandUseCase {

    User join(UserCreateCommand userCreateCommand);

}
