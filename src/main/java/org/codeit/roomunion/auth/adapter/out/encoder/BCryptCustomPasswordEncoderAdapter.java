package org.codeit.roomunion.auth.adapter.out.encoder;

import org.codeit.roomunion.auth.application.port.out.CustomPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptCustomPasswordEncoderAdapter implements CustomPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    public BCryptCustomPasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}