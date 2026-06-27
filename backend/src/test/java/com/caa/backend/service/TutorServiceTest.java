package com.caa.backend.service;

import com.caa.backend.dto.ResponseDTOs.AuthResponseDTO;
import com.caa.backend.dto.TutorRequestsDTOS.TutorLoginRequestDTO;
import com.caa.backend.dto.TutorRequestsDTOS.TutorRegisterRequestDTO;
import com.caa.backend.exception.ResourceNotFoundException;
import com.caa.backend.mapper.TutorMapper;
import com.caa.backend.model.Tutor;
import com.caa.backend.model.enums.Role;
import com.caa.backend.repository.TutorRepository;
import com.caa.backend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock private TutorRepository tutorRepository;
    @Mock private TutorMapper tutorMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserDetailsService userDetailsService;
    @Mock private UserDetails userDetails;

    @InjectMocks
    private TutorService tutorService;

    private TutorRegisterRequestDTO registerRequest;
    private TutorLoginRequestDTO loginRequest;
    private Tutor tutor;
    private AuthResponseDTO authResponse;

    private static final String EMAIL = "ana@example.com";
    private static final String PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encoded_password";
    private static final String JWT_TOKEN = "jwt.token.test";

    @BeforeEach
    void setUp() {
        registerRequest = new TutorRegisterRequestDTO("Ana García", EMAIL, PASSWORD);
        loginRequest = new TutorLoginRequestDTO(EMAIL, PASSWORD);

        tutor = new Tutor();
        tutor.setId(1L);
        tutor.setName("Ana García");
        tutor.setEmail(EMAIL);
        tutor.setPassword(ENCODED_PASSWORD);
        tutor.setRole(Role.TUTOR);

        // authResponse without token — service sets it after mapping
        authResponse = AuthResponseDTO.builder()
                .tutorId(1L)
                .name("Ana García")
                .email(EMAIL)
                .role(Role.TUTOR)
                .build();
    }

    /******* register **************************************************/
    @Test
    void shouldReturnAuthResponseWithToken_whenRegisterWithValidData() {
        when(tutorRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutor);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn(JWT_TOKEN);
        when(tutorMapper.toAuthResponse(tutor)).thenReturn(authResponse);

        AuthResponseDTO result = tutorService.register(registerRequest);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo(JWT_TOKEN);
        assertThat(result.getEmail()).isEqualTo(EMAIL);
        verify(tutorRepository).existsByEmail(EMAIL);
        verify(tutorRepository).save(any(Tutor.class));
        verify(passwordEncoder).encode(PASSWORD);
        verify(jwtUtil).generateToken(userDetails);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenRegisterWithDuplicateEmail() {
        when(tutorRepository.existsByEmail(EMAIL)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> tutorService.register(registerRequest)
        );
        assertThat(ex.getMessage()).contains(EMAIL);
        verify(tutorRepository, never()).save(any(Tutor.class));
    }

    /******* login *************************************************/
    @Test
    void shouldReturnAuthResponseWithToken_whenLoginWithValidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD));
        when(tutorRepository.findByEmail(EMAIL)).thenReturn(Optional.of(tutor));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn(JWT_TOKEN);
        when(tutorMapper.toAuthResponse(tutor)).thenReturn(authResponse);

        AuthResponseDTO result = tutorService.login(loginRequest);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo(JWT_TOKEN);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tutorRepository).findByEmail(EMAIL);
        verify(jwtUtil).generateToken(userDetails);
    }

    @Test
    void shouldThrowBadCredentialsException_whenLoginWithWrongPassword() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(
                BadCredentialsException.class,
                () -> tutorService.login(loginRequest)
        );
        verify(tutorRepository, never()).findByEmail(anyString());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenTutorNotFoundAfterAuthentication() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD));
        when(tutorRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> tutorService.login(loginRequest)
        );
        verify(jwtUtil, never()).generateToken(any());
    }
}
