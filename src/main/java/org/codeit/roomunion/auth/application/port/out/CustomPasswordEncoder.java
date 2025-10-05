package org.codeit.roomunion.auth.application.port.out;

public interface CustomPasswordEncoder {
    String encode(String rawPassword);
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======

>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
    boolean matches(String rawPassword, String encodedPassword);
}