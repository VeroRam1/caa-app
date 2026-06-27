package com.caa.backend.dto;

import com.caa.backend.model.enums.Level;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for changing a child's communication level independently
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeLevelRequestDTO {
    @NotNull(message = "El nivel es obligatorio")
    private Level level;
}
