package com.caa.backend.dto;

import com.caa.backend.model.enums.Level;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// DTO for child profile creation and update requests
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildProfileRequestDTO {
    @NotBlank(message = "El nombre del niño es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    private LocalDate birthDate;

    private String photoUrl;

    private Level level;

    public Level getLevelOrDefault() {
        return this.level != null ? this.level : Level.LEVEL_1;
    }
}
