package com.caa.backend.service;

import com.caa.backend.dto.ResponseDTOs.AuthResponseDTO;
import com.caa.backend.dto.TutorRequestsDTOS.TutorLoginRequestDTO;
import com.caa.backend.dto.TutorRequestsDTOS.TutorRegisterRequestDTO;
import com.caa.backend.exception.ResourceNotFoundException;
import com.caa.backend.mapper.TutorMapper;
import com.caa.backend.model.Tutor;
import com.caa.backend.repository.TutorRepository;
import com.caa.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TutorService {
    private final TutorRepository tutorRepository;
    private final TutorMapper tutorMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    /**
     * Register a new tutor account
     * @param request registration data (name, email, plain password)
     * @return AuthResponseDTO with JWT token and tutor info
     * @throws IllegalArgumentException if email is already registered
     */
    public AuthResponseDTO register(TutorRegisterRequestDTO request) {
        log.info("Registering new tutor with email: {}", request.getEmail());

        if (tutorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + request.getEmail());
        }

        // Sin @Builder — usar constructor o setters directamente
        Tutor tutor = new Tutor();
        tutor.setName(request.getName());
        tutor.setEmail(request.getEmail());
        tutor.setPassword(passwordEncoder.encode(request.getPassword()));

        // Guardar primero el tutor para que tenga ID antes de usarlo
        Tutor savedTutor = tutorRepository.save(tutor);
        log.info("Tutor registered successfully with ID: {}", savedTutor.getId());

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedTutor.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        AuthResponseDTO response = tutorMapper.toAuthResponse(savedTutor);
        response.setToken(token);
        return response;
    }

    /**
     * Authenticate a tutor and return a JWT token
     * @param request login data (email, plain password)
     * @return AuthResponseDTO with JWT token and tutor info
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials are invalid
     */
    public AuthResponseDTO login(TutorLoginRequestDTO request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // Delegates to Spring Security — throws BadCredentialsException if invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Tutor tutor = tutorRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found with email: " + request.getEmail()));

        log.info("Tutor logged in successfully with ID: {}", tutor.getId());

        // Generate JWT and build response
        UserDetails userDetails = userDetailsService.loadUserByUsername(tutor.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        AuthResponseDTO response = tutorMapper.toAuthResponse(tutor);
        response.setToken(token);
        return response;
    }

}
