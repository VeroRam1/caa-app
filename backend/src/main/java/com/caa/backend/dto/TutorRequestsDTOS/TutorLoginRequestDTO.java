package com.caa.backend.dto.TutorRequestsDTOS;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO for tutor login requests
 * Used in POST /api/auth/login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorLoginRequestDTO {
    /**
     * Email validated both here (backend safety) and in the frontend (UX).
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
