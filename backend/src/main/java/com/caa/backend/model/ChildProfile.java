package com.caa.backend.model;

import com.caa.backend.model.enums.Level;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "child_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private LocalDate birthDate;

    @Column(length = 500)
    private String photoUrl;

    /**
     * Communication level assigned to this child.
     * Determines board complexity and available features:
     *   LEVEL_1 → few pictograms and boards, no phrase construction
     *   LEVEL_2 → more pictograms and boards, phrase construction enabled
     *   LEVEL_3 → same as LEVEL_2 but with even more pictograms and boards
     * Defaults to LEVEL_1 on creation.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level = Level.LEVEL_1;

    //The tutor who owns and manages this profile. Cannot be null — every child profile must belong to a tutor.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    // Boards assigned to this child profile. A child can have multiple boards;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "child_profile_boards",
            joinColumns = @JoinColumn(name = "child_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "board_id")
    )
    private List<Board> assignedBoards = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void assignBoard(Board board) {
        if (!assignedBoards.contains(board)) {
            assignedBoards.add(board);
        }
    }

    public void removeBoard(Board board) {
        assignedBoards.remove(board);
    }

    // Phrase construction only available in levels 2 and 3
    public boolean hasPhraseConstruction() {
        return this.level == Level.LEVEL_2 || this.level == Level.LEVEL_3;
    }


}
