package org.codeit.roomunion.auth.domain.model;

import lombok.Getter;

@Getter
public class RefreshResult {

    private final String accessToken;

    private RefreshResult(String accessToken) {
        this.accessToken = accessToken;
    }

    public static RefreshResult of(String accessToken) {
        return new RefreshResult(accessToken);
    }
}
