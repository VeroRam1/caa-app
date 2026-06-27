package com.caa.backend.controller;

import com.caa.backend.dto.ResponseDTOs.APIResponseDTO;
import com.caa.backend.dto.ResponseDTOs.AuthResponseDTO;
import com.caa.backend.dto.TutorRequestsDTOS.TutorLoginRequestDTO;
import com.caa.backend.dto.TutorRequestsDTOS.TutorRegisterRequestDTO;
import com.caa.backend.service.TutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Tutor registration and login API")
public class AuthController {
    private final TutorService tutorService;

    // Register new tutor
    @PostMapping("/register")
    @Operation(summary = "Register a new tutor", description = "Creates a tutor account and returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Tutor registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already registered")
    })
    public ResponseEntity<APIResponseDTO<AuthResponseDTO>> register(
            @Valid @RequestBody TutorRegisterRequestDTO request) {
        log.info("POST /api/auth/register - Registering tutor with email: {}", request.getEmail());
        AuthResponseDTO authResponse = tutorService.register(request);
        APIResponseDTO<AuthResponseDTO> response = APIResponseDTO.success("Tutor registered successfully", authResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Login
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates with email and password and returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<APIResponseDTO<AuthResponseDTO>> login(
            @Valid @RequestBody TutorLoginRequestDTO request) {
        log.info("POST /api/auth/login - Login attempt for email: {}", request.getEmail());
        AuthResponseDTO authResponse = tutorService.login(request);
        APIResponseDTO<AuthResponseDTO> response = APIResponseDTO.success("Login successful", authResponse);
        return ResponseEntity.ok(response);
    }
}
