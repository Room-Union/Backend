package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.model.User;

public interface UserQueryUseCase {

    User getByEmail(String email);

}
