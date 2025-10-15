package org.codeit.roomunion.common.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum RedisCacheType {
    // 예시: 필요한 캐시 타입을 추가하세요
    USER("user", Duration.ofMinutes(30)),
    MEETING("meeting", Duration.ofMinutes(10)),
    MEETING_LIST("meetingList", Duration.ofMinutes(5));

    private final String cacheName;
    private final Duration ttl;
}