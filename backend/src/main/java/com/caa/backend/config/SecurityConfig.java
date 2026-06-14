package com.caa.backend.config;

import com.caa.backend.security.JwtAuthenticationFilter;
import com.caa.backend.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration.
 *
 * Security model:
 * - Stateless JWT authentication (no server-side sessions)
 * - Public endpoints: /api/auth/** (register, login)
 *                     /v3/api-docs/**, /swagger-ui/** (Swagger — your existing config)
 *                     /api/arasaac/** (your existing ARASAAC endpoint — adjust if needed)
 * - All other endpoints require a valid JWT
 */
@EnableCaching
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configure(http))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/arasaac/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/boards/predefined").permitAll()
                        .requestMatchers("/api/boards/level/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/boards/{id}").permitAll()
                        .anyRequest().authenticated()  // siempre el último, solo uno
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .headers(headers -> headers
                        // X-Frame-Options: prevents the app from being loaded in an <iframe> from another origin (clickjacking)
                        .frameOptions(frame -> frame.sameOrigin())

                        // X-Content-Type-Options: prevents the browser from guessing the MIME type (MIME sniffing)
                        .contentTypeOptions(contentType -> {})

                        // X-XSS-Protection: enables the browser's built-in XSS filter (legacy, but still recommended)
                        .xssProtection(xss -> {})

                        // Referrer-Policy: does not send the origin URL in cross-origin requests
                        .referrerPolicy(referrer ->
                                referrer.policy(org.springframework.security.web.header.writers
                                        .ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                        )

                        // Content-Security-Policy: defines which resources the app is allowed to load
                        // Configured for Angular + PrimeNG + ARASAAC + Google Fonts
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline'; " +  // unsafe-inline required for Angular in dev
                                        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                                        "font-src 'self' https://fonts.gstatic.com; " +
                                        "img-src 'self' data: https://static.arasaac.org https://api.arasaac.org; " +
                                        "connect-src 'self' http://localhost:8080 https://api.arasaac.org; " +
                                        "frame-ancestors 'none';"  // reinforces X-Frame-Options
                        ))

                        // Permissions-Policy: disables browser APIs that the app does not use
                        .addHeaderWriter(new org.springframework.security.web.header.writers
                                        .StaticHeadersWriter(
                                        "Permissions-Policy",
                                        "camera=(), microphone=(), geolocation=(), payment=()"
                                )
                        )
                )

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    // BCrypt password encoder. Used both here and injected into TutorService for hashing passwords on register.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}