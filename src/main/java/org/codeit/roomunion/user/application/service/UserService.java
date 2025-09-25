package org.codeit.roomunion.user.application.service;

import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserQueryUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }
}
