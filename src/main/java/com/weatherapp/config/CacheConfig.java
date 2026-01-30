package com.weatherapp.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class CacheConfig implements CachingConfigurer {

    @Value("${weather.cache.ttl-minutes}")
    private long cacheTtlMinutes;

    @Bean
    @Override
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "currentWeather",
                "hourlyForecast",
                "dailyForecast"
        );
        
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(cacheTtlMinutes, TimeUnit.MINUTES)
                .recordStats()
                .removalListener((key, value, cause) -> 
                    log.debug("Cache entry removed: key={}, cause={}", key, cause)
                ));
        
        return cacheManager;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, 
                                          org.springframework.cache.Cache cache, 
                                          Object key) {
                log.error("Cache get error for key {} in cache {}", key, cache.getName(), exception);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, 
                                          org.springframework.cache.Cache cache, 
                                          Object key, 
                                          Object value) {
                log.error("Cache put error for key {} in cache {}", key, cache.getName(), exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, 
                                            org.springframework.cache.Cache cache, 
                                            Object key) {
                log.error("Cache evict error for key {} in cache {}", key, cache.getName(), exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, 
                                            org.springframework.cache.Cache cache) {
                log.error("Cache clear error in cache {}", cache.getName(), exception);
            }
        };
    }
}
