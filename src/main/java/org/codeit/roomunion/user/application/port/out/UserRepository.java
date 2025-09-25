package org.codeit.roomunion.user.application.port.out;

import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;

public interface UserRepository {

    User getByEmail(String email);

    User create(UserCreateCommand userCreateCommand);

}
