package org.codeit.roomunion.auth.domain.model;

import lombok.Getter;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class LoginUserDetails implements CustomUserDetails {

    private final User user;

    private LoginUserDetails(User user) {
        this.user = user;
    }

    public static LoginUserDetails from(User user) {
        return new LoginUserDetails(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    public Long getId() {
        return user.getId();
    }
}
