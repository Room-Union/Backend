package org.codeit.roomunion.auth.application.port.out;

public interface CustomPasswordEncoder {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);

}