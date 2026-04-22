package com.caa.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials (cookies, authorization headers)
        config.setAllowCredentials(true);

        // Allow requests from these origins
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:4200",     // Angular default dev server
                "http://localhost:*",         // Any local port
                "http://127.0.0.1:*"          // Localhost alternative
        ));

        // Allow these HTTP methods
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        // Allow all headers
        config.setAllowedHeaders(List.of("*"));

        // Expose these headers to the client
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Total-Count"
        ));

        // Cache preflight requests for 1 hour
        config.setMaxAge(3600L);

        // Apply CORS configuration to all endpoints
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
