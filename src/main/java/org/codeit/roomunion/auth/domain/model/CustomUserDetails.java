package org.codeit.roomunion.auth.domain.model;

import org.codeit.roomunion.user.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {

    boolean isLoggedIn();

    User getUser();

}