package org.codeit.roomunion.auth.application.service;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
<<<<<<< HEAD
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
=======
        User user = userRepository.getByEmail(email);
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
        return new CustomUserDetails(user);
    }
}
