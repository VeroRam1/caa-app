package com.caa.backend.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that manages rate limiting buckets per user or IP.
 *
 * Two strategies:
 * - Authenticated endpoints: limit by user email (extracted from JWT)
 * - Auth endpoints (login/register): limit by IP (no JWT available yet)
 */
@Service
@Slf4j
public class RateLimitingService {

    // Buckets for authenticated users — keyed by email
    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    // Buckets for auth endpoints — keyed by IP
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

    // General API: 100 requests per minute per authenticated user.
    public Bucket resolveBucketForUser(String email) {
        return userBuckets.computeIfAbsent(email, k -> createGeneralBucket());
    }

    // Auth endpoints: 10 requests per minute per IP. Prevents brute-force attacks on login and registration.
    public Bucket resolveBucketForIp(String ip) {
        return ipBuckets.computeIfAbsent(ip, k -> createAuthBucket());
    }

    private Bucket createGeneralBucket() {
        Bandwidth limit = Bandwidth.classic(
                100, Refill.intervally(100, Duration.ofMinutes(1))
        );
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket createAuthBucket() {
        Bandwidth limit = Bandwidth.classic(
                10, Refill.intervally(10, Duration.ofMinutes(1))
        );
        return Bucket.builder().addLimit(limit).build();
    }
}

