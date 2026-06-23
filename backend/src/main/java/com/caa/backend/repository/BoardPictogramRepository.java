package com.caa.backend.repository;

import com.caa.backend.model.BoardPictogram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardPictogramRepository extends JpaRepository<BoardPictogram, Long> {
    List<BoardPictogram> findBoardById(Long boardId);
    List<BoardPictogram> findByBoardIdOrderByPositionYAscPositionXAsc(Long boardId);
    Optional<BoardPictogram> findByBoardIdAndPositionXAndPositionY(
            Long boardId, Integer positionX, Integer positionY
    );
    boolean existsByBoardIdAndPositionXAndPositionY(
            Long boardId, Integer positionX, Integer positionY
    );
    List<BoardPictogram> findByPictogramId(Integer pictogramId);
    List<BoardPictogram> findByTextContainingIgnoreCase(String text);
    List<BoardPictogram> findByBoardIdAndPositionY(Long boardId, Integer positionY);
    List<BoardPictogram> findByBoardIdAndPositionX(Long boardId, Integer positionX);
    Long countByBoardId(Long boardId);
    void deleteByBoardId(Long boardId);
    List<BoardPictogram> findByBackgroundColor(String backColor);
    @Query("SELECT p.pictogramId, COUNT(p) as total " +
            "FROM BoardPictogram p " +
            "GROUP BY p.pictogramId " +
            "ORDER BY total DESC")
    List<Object[]> findMostUsedPictogram();

    @Query("SELECT p FROM BoardPictogram p JOIN FETCH p.board WHERE p.board.id = :boardId")
    List<BoardPictogram> findByBoardIdWithBoard(@Param("boardId") Long boardId);

    @Query(value = "SELECT x.pos_x, y.pos_y " +
            "FROM (SELECT DISTINCT position_x as pos_x FROM board_pictograms WHERE board_id = :boardId) x " +
            "CROSS JOIN (SELECT DISTINCT position_y as pos_y FROM board_pictograms WHERE board_id = :boardId) y " +
            "WHERE NOT EXISTS (" +
            "  SELECT 1 FROM board_pictograms " +
            "  WHERE board_id = :boardId " +
            "  AND position_x = x.pos_x " +
            "  AND position_y = y.pos_y" +
            ")",
            nativeQuery = true)
    List<Object[]> findEmptyPositions(@Param("boardId") Long boardId);
}
