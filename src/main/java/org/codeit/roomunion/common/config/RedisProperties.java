package org.codeit.roomunion.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    private final RedisProperties redisProperties;

    public RedisProperties(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    public static class RedisProperty {
        private final String host;
        private final int port;
        private final String password;

        public RedisProperty(String host, int port, String password) {
            this.host = host;
            this.port = port;
            this.password = password;
        }
    }
}