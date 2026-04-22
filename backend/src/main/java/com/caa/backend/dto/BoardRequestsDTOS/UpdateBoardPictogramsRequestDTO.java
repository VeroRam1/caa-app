package com.caa.backend.dto.BoardRequestsDTOS;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBoardPictogramsRequestDTO {
    @NotNull(message = "La lista de pictogramas es obligatoria")
    List<PictogramPositionDTO> pictograms;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PictogramPositionDTO {
        private Integer pictogramId; // ARASAAC pictogram ID
        private String pictogramUrl; // ARASAAC static image URL
        private String text;
        private Integer positionX;
        private Integer positionY;
        private String backgroundColor;
    }
}
