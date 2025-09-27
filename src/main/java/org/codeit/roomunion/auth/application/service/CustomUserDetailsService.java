package org.codeit.roomunion.auth.application.service;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserQueryUseCase userQueryUseCase;

    public CustomUserDetailsService(UserQueryUseCase userQueryUseCase) {
        this.userQueryUseCase = userQueryUseCase;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userQueryUseCase.getByEmail(email);
        return new CustomUserDetails(user);
    }
}
