package org.codeit.roomunion.auth.adapter.in.web.request;

public record VerifyEmailCodeRequest(String email, String code) {
}
