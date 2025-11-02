package org.codeit.roomunion.auth.domain.model;

import lombok.Getter;

@Getter
public class RefreshResult {

    private final String accessToken;
    private final String accessToken2;

    private RefreshResult(String accessToken, String accessToken2) {
        this.accessToken = accessToken;
        this.accessToken2 = accessToken2;
    }

    public static RefreshResult of(String accessToken, String accessToken2) {
        return new RefreshResult(accessToken, accessToken2);
    }
}
