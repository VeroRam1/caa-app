package com.caa.backend.dto.PictogramRequestsDTOS;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for adding a pictogram to a board
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPictogramRequest {
    @NotNull(message = "Pictogram ID is required")
    private Integer pictogramId;

    @NotBlank(message = "Pictogram URL is required")
    @Size(max = 500, message = "URL cannot exceed 500 characters")
    private String pictogramUrl;

    @NotBlank(message = "Text is required")
    @Size(max = 100, message = "Text cannot exceed 100 characters")
    private String text;

    @NotNull(message = "X position is required")
    @Min(value = 0, message = "X position must be greater than or equal to 0")
    private Integer positionX;

    @NotNull(message = "Y position is required")
    @Min(value = 0, message = "Y position must be greater than or equal to 0")
    private Integer positionY;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Background color must be in hexadecimal format (#RRGGBB)")
    private String backgroundColor;

    // Constructor without background color (defaults to white)
    public AddPictogramRequest(Integer pictogramId, String pictogramUrl, String text,
                               Integer positionX, Integer positionY) {
        this.pictogramId = pictogramId;
        this.pictogramUrl = pictogramUrl;
        this.text = text;
        this.positionX = positionX;
        this.positionY = positionY;
        this.backgroundColor = "#FFFFFF";
    }
}
