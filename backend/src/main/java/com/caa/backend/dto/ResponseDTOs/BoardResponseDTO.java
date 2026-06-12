package com.caa.backend.dto.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// DTO for Board responses
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer rows;
    private Integer columns;
    private Integer level;
    private Boolean isPredefined;
    private Integer totalCells;
    private Integer pictogramCount;
    private List<PictogramResponseDTO> pictograms;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor without pictograms (for list views)
    public BoardResponseDTO(Long id, String name, String description, Integer rows,
                         Integer columns, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rows = rows;
        this.columns = columns;
        this.totalCells = rows * columns;
        this.pictogramCount = 0;
        this.pictograms = new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setPictograms(List<PictogramResponseDTO> pictograms) {
        this.pictograms = pictograms;
        this.pictogramCount = pictograms != null ? pictograms.size() : 0;
    }

    // Calculates if the board is full
    public boolean isFull() {
        return pictogramCount != null && pictogramCount.equals(totalCells);
    }

    // Calculates if the board is empty

    public boolean isEmpty() {
        return pictogramCount == null || pictogramCount == 0;
    }

    // Gets the percentage of occupied cells
    public double getOccupancyPercentage() {
        if (totalCells == null || totalCells == 0) return 0.0;
        return (pictogramCount * 100.0) / totalCells;
    }
}
