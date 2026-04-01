package com.caa.backend.dto.BoardRequestsDTOS;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing board
 * Used in PUT /api/boards/{id}
 * All fields are optional (null values won't be updated)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardRequestDTO {
    @Size(min = 3, max = 100, message = "Board name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Min(value = 1, message = "Board must have at least 1 row")
    @Max(value = 10, message = "Board cannot have more than 10 rows")
    private Integer rows;

    @Min(value = 1, message = "Board must have at least 1 column")
    @Max(value = 10, message = "Board cannot have more than 10 columns")
    private Integer columns;

    @Min(value = 1, message = "Level must be 1, 2, or 3")
    @Max(value = 3, message = "Level must be 1, 2, or 3")
    private Integer level;

    /**
     * Helper method to check if any field is present
     * @return true if at least one field is not null
     */
    public boolean hasAnyField() {
        return name != null || description != null || rows != null || columns != null || level != null;
    }
}
