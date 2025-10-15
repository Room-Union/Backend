package org.codeit.roomunion.common.config.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * REDIS CACHE TYPE
 * 캐시 이름과 TTL(Time To Live)을 정의하는 열거형 클래스입니다.
 * 각 캐시 타입은 고유한 이름과 만료 시간을 가집니다.
 * 예를 들어, LOGIN 캐시는 "login"이라는 이름과 5초의 TTL을 가집니다.(제거 예정)
 */
@Getter
@RequiredArgsConstructor
public enum RedisCacheType {

    LOGIN("login", Duration.ofSeconds(10)), // FIXME 제거 예정
    ;

    private final String cacheName;
    private final Duration ttl;

}
