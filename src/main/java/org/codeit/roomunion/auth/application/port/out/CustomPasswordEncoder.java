package org.codeit.roomunion.auth.application.port.out;

public interface CustomPasswordEncoder {
    String encode(String rawPassword);
<<<<<<< HEAD

=======
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
    boolean matches(String rawPassword, String encodedPassword);
}