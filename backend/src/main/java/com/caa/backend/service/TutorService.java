package com.caa.backend.service;

import com.caa.backend.dto.ResponseDTOs.AuthResponseDTO;
import com.caa.backend.dto.TutorRequestsDTOS.TutorLoginRequestDTO;
import com.caa.backend.dto.TutorRequestsDTOS.TutorRegisterRequestDTO;
import com.caa.backend.exception.ResourceNotFoundException;
import com.caa.backend.mapper.TutorMapper;
import com.caa.backend.model.Tutor;
import com.caa.backend.repository.TutorRepository;
import com.caa.backend.security.JwtUtil;
import com.caa.backend.security.SanitizationUtils;
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

    // Register a new tutor account
    public AuthResponseDTO register(TutorRegisterRequestDTO request) {
        log.info("Registering new tutor with email: {}", request.getEmail());

        if (tutorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + request.getEmail());
        }

        Tutor tutor = new Tutor();
        tutor.setName(SanitizationUtils.sanitize(request.getName()));
        tutor.setEmail(SanitizationUtils.sanitize(request.getEmail()));
        tutor.setPassword(passwordEncoder.encode(request.getPassword()));

        Tutor savedTutor = tutorRepository.save(tutor);
        log.info("Tutor registered successfully with ID: {}", savedTutor.getId());

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedTutor.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        AuthResponseDTO response = tutorMapper.toAuthResponse(savedTutor);
        response.setToken(token);
        return response;
    }

    // Authenticate a tutor and return a JWT token
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
