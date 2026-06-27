package com.caa.backend.dto.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for Pictogram responses. Represents a pictogram on a board
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PictogramResponseDTO {
    private Long id;
    private Integer pictogramId;
    private String pictogramUrl;
    private String text;
    private Integer positionX;
    private Integer positionY;
    private String backgroundColor;

    // Gets the pictogram URL with custom options
    public String getPictogramUrlWithOptions(int size, boolean withBackground) {
        return String.format(
                "https://api.arasaac.org/v1/pictograms/%d?download=false&plural=false&color=true&backgroundColor=%s&size=%d",
                pictogramId, withBackground ? "true" : "false", size
        );
    }

    // Checks if the pictogram has a custom background color
    public boolean hasCustomBackground() {
        return backgroundColor != null && !backgroundColor.equalsIgnoreCase("#FFFFFF");
    }
}
