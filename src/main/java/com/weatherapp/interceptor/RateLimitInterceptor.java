package com.weatherapp.interceptor;

import com.weatherapp.exception.RateLimitExceededException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> rateLimitBuckets;
    private final Supplier<BucketConfiguration> bucketConfiguration;

    @Value("${weather.rate-limit.enabled}")
    private boolean rateLimitEnabled;

    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) {
        if (!rateLimitEnabled) {
            return true;
        }

        String clientId = getClientIdentifier(request);
        
        Bucket bucket = rateLimitBuckets.computeIfAbsent(clientId, 
            key -> Bucket.builder()
                .addLimit(bucketConfiguration.get().getBandwidths()[0])
                .build());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", 
                             String.valueOf(probe.getRemainingTokens()));
            log.debug("Request allowed for client: {} (remaining: {})", 
                     clientId, probe.getRemainingTokens());
            return true;
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
        log.warn("Rate limit exceeded for client: {} (retry after: {}s)", 
                clientId, waitForRefill);
        
        throw new RateLimitExceededException(
                "Rate limit exceeded. Please try again later.", 
                waitForRefill
        );
    }

    private String getClientIdentifier(HttpServletRequest request) {
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey != null && !apiKey.isBlank()) {
            return "api_key:" + apiKey;
        }

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isBlank()) {
            clientIp = request.getRemoteAddr();
        } else {
            clientIp = clientIp.split(",")[0].trim();
        }

        return "ip:" + clientIp;
    }
}
