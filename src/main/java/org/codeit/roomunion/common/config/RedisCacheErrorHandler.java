package org.codeit.roomunion.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Slf4j
public class RedisCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.error("Redis Cache Get Error - Cache: {}, Key: {}, Message: {}",
                cache.getName(), key, exception.getMessage(), exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.error("Redis Cache Put Error - Cache: {}, Key: {}, Message: {}",
                cache.getName(), key, exception.getMessage(), exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.error("Redis Cache Evict Error - Cache: {}, Key: {}, Message: {}",
                cache.getName(), key, exception.getMessage(), exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("Redis Cache Clear Error - Cache: {}, Message: {}",
                cache.getName(), exception.getMessage(), exception);
    }
}