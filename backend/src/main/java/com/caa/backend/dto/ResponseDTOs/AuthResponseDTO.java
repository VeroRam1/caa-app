package com.caa.backend.dto.ResponseDTOs;
import com.caa.backend.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO for authentication responses (register and login)
 * Used in POST /api/auth/register and POST /api/auth/login
 * Never exposes the hashed password
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    /**
     * JWT token to be sent in subsequent requests
     * as "Authorization: Bearer <token>"
     */
    private String token;

    private Long tutorId;
    private String name;
    private String email;
    private Role role;
}
