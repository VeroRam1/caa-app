package com.caa.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "boards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del tablero es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Column(length = 500)
    private String description;

    @Min(value = 1, message = "Debe haber al menos una fila")
    @Max(value = 10, message = "No puede haber más de 10 filas")
    @Column(nullable = false)
    private Integer rows = 3;

    @Min(value = 1, message = "Debe haber al menos una columna")
    @Max(value = 10, message = "No puede haber más de 10 columnas")
    @Column(nullable = false)
    private Integer columns = 4;

    /**
     * Difficulty level: 1 (Basic), 2 (Intermediate), 3 (Advanced)
     * Determines which users can access this board
     */
    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 3, message = "Level cannot exceed 3")
    @Column(nullable = false)
    private Integer level = 1;  // Default to basic level

    /**
     * Indicates if this is a predefined/template board
     * Predefined boards cannot be deleted by users
     */
    @Column(nullable = false)
    private Boolean isPredefined = false;

    /**
     * Relation one to many with BoardPictogram
     * One board can have multiple pictograms
     * cascade = CascadeType.ALL: cuando se elimina el tablero, se eliminan sus pictogramas ???
     * orphanRemoval = true: si se elimina un pictograma de la lista, se elimina de la BD ???
     */

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardPictogram> pictograms = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Tutor that owns the board
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    private Tutor owner;

    /**
     * Calcula el número total de celdas del tablero
     * @return filas * columnas
     */
    public Integer getTotalCells(){
        return this.rows * this.columns;
    }

    /**
     * Verifica si una posición es válida dentro del tablero
     * @param x posición en columna (0-based)
     * @param y posición en fila (0-based)
     * @return true si la posición está dentro del rango
     */
    public boolean isValidPosition(int x, int y){
        return x >= 0 && x < this.columns && y >= 0 && y < this.rows;
    }

    /**
     * Añade un pictograma al tablero
     * @param pictogram el pictograma a añadir
     */
    public void addPictogram(BoardPictogram pictogram){
        pictograms.add(pictogram);
        //pictogram.setBoard(this);
    }

    /**
     * Elimina un pictograma del tablero
     * @param pictogram el pictograma a eliminar
     */
    public void removePictogram(BoardPictogram pictogram){
        pictograms.remove(pictogram);
        pictogram.setBoard(null);
    }


}
