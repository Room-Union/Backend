package org.codeit.roomunion.user.adapter.in.web.response;

import org.codeit.roomunion.user.domain.model.User;

public record JoinUserResponse(long id) {
    public static JoinUserResponse from(User user) {
        return new JoinUserResponse(user.getId());
    }
}
