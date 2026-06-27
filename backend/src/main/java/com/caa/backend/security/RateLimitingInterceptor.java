package com.caa.backend.security;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// MVC interceptor that enforces rate limiting. Returns HTTP 429 Too Many Requests when the limit is exceeded.
@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RateLimitingService rateLimitingService;

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {

        String path = request.getRequestURI();
        Bucket bucket;

        if (path.startsWith("/api/auth/")) {
            // Auth endpoints — limit by IP
            String ip = getClientIp(request);
            bucket = rateLimitingService.resolveBucketForIp(ip);
            log.debug("Rate limit check — auth endpoint, IP: {}", ip);
        } else {
            // Authenticated endpoints — limit by user email
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() ||
                    auth.getPrincipal().equals("anonymousUser")) {
                // Not authenticated — let Spring Security handle it
                return true;
            }
            String email = auth.getName();
            bucket = rateLimitingService.resolveBucketForUser(email);
            log.debug("Rate limit check — user: {}", email);
        }

        if (bucket.tryConsume(1)) {
            return true; // Allow request
        }

        // Limit exceeded — reject with 429
        log.warn("Rate limit exceeded — path: {}, user/IP: {}",
                path,
                path.startsWith("/api/auth/") ? getClientIp(request) :
                        SecurityContextHolder.getContext().getAuthentication().getName()
        );

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
            {
                "success": false,
                "message": "Demasiadas peticiones. Por favor, espera un momento antes de volver a intentarlo.",
                "status": 429
            }
            """);
        return false; // Block request
    }

    // Extracts the real client IP, accounting for reverse proxies.
    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}