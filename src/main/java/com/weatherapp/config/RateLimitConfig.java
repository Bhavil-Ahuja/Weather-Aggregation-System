package com.weatherapp.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Configuration
public class RateLimitConfig {

    @Value("${weather.rate-limit.requests-per-minute}")
    private long requestsPerMinute;

    @Bean
    public Map<String, Bucket> rateLimitBuckets() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Supplier<BucketConfiguration> bucketConfiguration() {
        return () -> {
            Bandwidth limit = Bandwidth.simple(requestsPerMinute, Duration.ofMinutes(1));
            return BucketConfiguration.builder()
                    .addLimit(limit)
                    .build();
        };
    }
}
