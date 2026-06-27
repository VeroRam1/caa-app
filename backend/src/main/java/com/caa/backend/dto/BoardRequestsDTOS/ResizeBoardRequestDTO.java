package com.caa.backend.dto.BoardRequestsDTOS;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResizeBoardRequestDTO {
    @NotNull(message = "El número de filas es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 fila")
    @Max(value = 8, message = "No puede haber más de 8 filas")
    private Integer rows;

    @NotNull(message = "El número de columnas es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 columna")
    @Max(value = 8, message = "No puede haber más de 8 columnas")
    private Integer columns;
}
