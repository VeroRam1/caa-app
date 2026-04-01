package com.caa.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
        name = "board_pictograms",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "position_x", "position_y"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardPictogram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonIgnore
    private Board board;

    @NotNull(message = "Pictogram ID is required")
    @Column(nullable = false)
    private Integer pictogramId;

    @NotBlank(message = "Pictogram URL is required")
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String pictogramUrl;

    @NotBlank(message = "Pictogram text is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String text;

    @NotNull(message = "X position is required")
    @Min(value = 0)
    @Column(name = "position_x", nullable = false)
    private Integer positionX;

    @NotNull(message = "Y position is required")
    @Min(value = 0)
    @Column(name = "position_y", nullable = false)
    private Integer positionY;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$")
    @Column(length = 7)
    private String backgroundColor = "#FFFFFF";

    public BoardPictogram(Board board, Integer pictogramId, String pictogramUrl,
                          String text, Integer positionX, Integer positionY) {
        this.board = board;
        this.pictogramId = pictogramId;
        this.pictogramUrl = pictogramUrl;
        this.text = text;
        this.positionX = positionX;
        this.positionY = positionY;
        this.backgroundColor = "#FFFFFF";
    }

    public boolean isValidPosition() {
        if (board == null) return false;
        return board.isValidPosition(this.positionX, this.positionY);
    }

    public void moveTo(int x, int y) {
        if (board != null && board.isValidPosition(x, y)) {
            this.positionX = x;
            this.positionY = y;
        } else {
            throw new IllegalArgumentException(
                    String.format("Invalid position: (%d, %d)", x, y)
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardPictogram)) return false;
        BoardPictogram that = (BoardPictogram) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}