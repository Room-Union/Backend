package org.codeit.roomunion.common.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisCacheConfig implements CachingConfigurer {
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 단일 Redis 서버 설정
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(redisProperties.getHost());
        standaloneConfiguration.setPort(redisProperties.getPort());

        // 패스워드가 설정되어 있는 경우에만 설정
        if (redisProperties.getPassword() != null && !redisProperties.getPassword().isEmpty()) {
            standaloneConfiguration.setPassword(redisProperties.getPassword());
        }

        // 소켓 옵션 설정
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofMillis(1000L))
                .keepAlive(true)
                .build();

        // 클라이언트 옵션 설정
        ClientOptions clientOptions = ClientOptions.builder()
                .socketOptions(socketOptions)
                .build();

        // Lettuce 클라이언트 설정
        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .clientOptions(clientOptions)
                .commandTimeout(Duration.ofMillis(3000L))
                .build();

        return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
    }

    @Bean
    public CacheManager redisCacheManager(
            RedisConnectionFactory redisConnectionFactory,
            GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(createDefaultRedisCache(genericJackson2JsonRedisSerializer))
                .withInitialCacheConfigurations(createRedisCache(genericJackson2JsonRedisSerializer))
                .build();
    }

    private RedisCacheConfiguration createDefaultRedisCache(GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer))
                .entryTtl(Duration.ofMinutes(1L));
    }

    private Map<String, RedisCacheConfiguration> createRedisCache(GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        Arrays.stream(RedisCacheType.values())
                .forEach(cacheType -> cacheConfigurations.put(
                        cacheType.getCacheName(),
                        createDefaultRedisCache(genericJackson2JsonRedisSerializer).entryTtl(cacheType.getTtl())
                ));
        return cacheConfigurations;
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        genericJackson2JsonRedisSerializer.configure(objectMapper -> {
            BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                    .allowIfSubType(Object.class)
                    .build();

            objectMapper.registerModule(new JavaTimeModule())
                    .activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                    .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)          // 모든 필드에 대해 직렬화/역직렬화 가능하도록 설정
                    .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)        // getter 메서드는 직렬화/역직렬화에서 제외
                    .setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);    // is getter 메서드는 직렬화/역직렬화에서 제외
        });

        return genericJackson2JsonRedisSerializer;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }
}