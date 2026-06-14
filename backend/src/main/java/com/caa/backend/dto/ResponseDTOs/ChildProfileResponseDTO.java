package com.caa.backend.dto.ResponseDTOs;

import com.caa.backend.model.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// DTO for child profile responses
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildProfileResponseDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String photoUrl;
    private Level level;
    private Long tutorId;
    private Integer boardCount;
    private List<BoardResponseDTO> assignedBoards;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor without boards (for list views)
    public ChildProfileResponseDTO(Long id, String name, LocalDate birthDate,
                                   String photoUrl, Level level, Long tutorId,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.photoUrl = photoUrl;
        this.level = level;
        this.tutorId = tutorId;
        this.boardCount = 0;
        this.assignedBoards = new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Sets assigned boards and updates the count
    public void setAssignedBoards(List<BoardResponseDTO> assignedBoards) {
        this.assignedBoards = assignedBoards;
        this.boardCount = assignedBoards != null ? assignedBoards.size() : 0;
    }

    // Checks whether this child has access to phrase construction. Available from LEVEL_2 onwards.
    public boolean hasPhraseConstruction() {
        return this.level == Level.LEVEL_2 || this.level == Level.LEVEL_3;
    }

    // Checks whether the child has any boards assigned
    public boolean hasBoards() {
        return boardCount != null && boardCount > 0;
    }
}
