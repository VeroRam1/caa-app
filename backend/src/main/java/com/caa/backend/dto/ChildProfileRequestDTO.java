package com.caa.backend.dto;

import com.caa.backend.model.enums.Level;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for child profile creation and update requests
 * Used in POST /api/child-profiles and PUT /api/child-profiles/{id}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildProfileRequestDTO {
    @NotBlank(message = "El nombre del niño es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    /**
     * Optional — used to display age or track development over time
     */
    private LocalDate birthDate;

    /**
     * Optional URL pointing to the child's profile picture
     */
    private String photoUrl;

    /**
     * Communication level for this child profile.
     * Defaults to LEVEL_1 if not provided.
     * Can be changed later via PATCH /api/child-profiles/{id}/level
     */
    private Level level;

    /**
     * Returns the level, defaulting to LEVEL_1 if not provided
     * @return level or LEVEL_1 as fallback
     */
    public Level getLevelOrDefault() {
        return this.level != null ? this.level : Level.LEVEL_1;
    }
}
