package org.codeit.roomunion.auth.domain.model;

import org.codeit.roomunion.user.domain.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UnknownUserDetails implements CustomUserDetails {

    private static final UnknownUserDetails INSTANCE = new UnknownUserDetails();
    private final User user;

    private UnknownUserDetails() {
        this.user = User.empty();
    }

    public static UnknownUserDetails getInstance() {
        return INSTANCE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public User getUser() {
        return user;
    }
}