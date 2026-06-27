package com.caa.backend.dto.PictogramRequestsDTOS;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for adding a pictogram to a board
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePictogramRequestDTO {
    @Min(value = 0, message = "X position must be greater than or equal to 0")
    private Integer positionX;

    @Min(value = 0, message = "Y position must be greater than or equal to 0")
    private Integer positionY;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Background color must be in hexadecimal format (#RRGGBB)")
    private String backgroundColor;

    @Size(max = 100, message = "Text cannot exceed 100 characters")
    private String text;

    public boolean isPositionUpdate() {
        return positionX != null || positionY != null;
    }

    public boolean isColorUpdate() {
        return backgroundColor != null;
    }

    public boolean hasAnyField() {
        return positionX != null || positionY != null ||
                backgroundColor != null || text != null;
    }
}
