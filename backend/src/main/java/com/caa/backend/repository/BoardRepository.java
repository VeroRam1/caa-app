package com.caa.backend.repository;

import com.caa.backend.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones CRUD sobre la entidad Tablero
 * JpaRepository proporciona métodos básicos: save, findById, findAll, delete, etc.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByName(String name);
    List<Board> findByNameContainingIgnoreCase(String name);
    List<Board> findByRowsAndColumns(Integer rows, Integer columns);
    List<Board> findByRows(Integer rows);
    List<Board> findByColumns(Integer columns);
    List<Board> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);
    List<Board> findByLevel(Integer level);
    List<Board> findByIsPredefined(Boolean isPredefined);
    List<Board> findByLevelOrderByCreatedAtDesc(Integer level);
    Long countByRowsAndColumns(Integer rows, Integer columns);
    boolean existsByName(String name);
    List<Board> findAllByOrderByCreatedAtDesc();
    List<Board> findAllByOrderByUpdatedAtDesc();

    /**
     * Obtiene un tablero con todos sus pictogramas cargados (evita N+1 queries)
     * @param id ID del tablero
     * @return Optional con el tablero y sus pictogramas
     */
    @Query("SELECT t FROM Board t LEFT JOIN FETCH t.pictograms WHERE t.id = :id")
    Optional<Board> findByIdWithPictograms(@Param("id") Long id);

    /**
     * Obtiene todos los tableros con sus pictogramas cargados
     * @return lista de tableros con pictogramas
     */
    @Query("SELECT DISTINCT t FROM Board t LEFT JOIN FETCH t.pictograms")
    List<Board> findAllWithPictograms();

    /**
     * Find boards with at least one pictogram
     * @return list of boards with pictograms
     */
    @Query("SELECT DISTINCT t FROM Board t WHERE SIZE(t.pictograms) > 0")
    List<Board> findBoardsWithPictograms();

    /**
     * Find empty boards (without pictograms)
     * @return lists empty boards
     */
    @Query("SELECT t FROM Board t WHERE SIZE(t.pictograms) = 0")
    List<Board> findEmptyBoards();


    /**
     * Cuenta el número total de pictogramas en un tablero
     * @param boardId ID del tablero
     * @return número de pictogramas
     */
    @Query("SELECT COUNT(p) FROM BoardPictogram p WHERE p.board.id = :boardId")
    Long countPictogramsByBoardId(@Param("boardID") Long boardId);

}
