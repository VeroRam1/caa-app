package com.caa.backend.service;

import com.caa.backend.dto.BoardRequestsDTOS.CreateBoardRequestDTO;
import com.caa.backend.dto.BoardRequestsDTOS.UpdateBoardRequestDTO;
import com.caa.backend.dto.ResponseDTOs.BoardResponseDTO;
import com.caa.backend.mapper.BoardMapper;
import com.caa.backend.model.Board;
import com.caa.backend.model.BoardPictogram;
import com.caa.backend.repository.BoardRepository;
import com.caa.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    /**
     * Get all boards
     * @return list of all boards without pictograms (for performance)
     */
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> getAllBoards(){
        log.info("Fetching all boards");
        List<Board> boards = boardRepository.findAll();
        return boardMapper.toResponseList(boards);
    }

    /**
     * Get a specific board by ID with all its pictograms
     * @param id board ID
     * @return board with pictograms
     * @throws ResourceNotFoundException if board doesn't exist
     */
    @Transactional(readOnly = true)
    public BoardResponseDTO getBoardById(Long id) {
        log.info("Fetching board with ID: {}", id);
        Board board = boardRepository.findByIdWithPictograms(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with ID: " + id));

        return boardMapper.toResponseWithPictograms(board);
    }

    /**
     * Create a new board
     * @param request board creation data
     * @return created board
     */
    public BoardResponseDTO createBoard(CreateBoardRequestDTO request) {
        log.info("Creating new board: {}", request.getName());

        // Convert DTO to entity using mapper
        Board board = boardMapper.toEntity(request);

        // Save to database
        Board savedBoard = boardRepository.save(board);
        log.info("Board created successfully with ID: {}", savedBoard.getId());

        return boardMapper.toResponse(savedBoard);
    }

    /**
     * Update an existing board
     * Only updates non-null fields from the request
     * @param id board ID
     * @param request update data
     * @return updated board
     * @throws ResourceNotFoundException if board doesn't exist
     */
    public BoardResponseDTO updateBoard(Long id, UpdateBoardRequestDTO request) {
        log.info("Updating board with ID: {}", id);

        // Find existing board
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with ID: " + id));

        // Validate resize if rows or columns are changing
        if (request.getRows() != null || request.getColumns() != null) {
            Integer newRows = request.getRows() != null ? request.getRows() : board.getRows();
            Integer newColumns = request.getColumns() != null ? request.getColumns() : board.getColumns();
            validateBoardResize(board, newRows, newColumns);
        }

        // Update entity using mapper (only non-null fields)
        boardMapper.updateEntityFromRequest(request, board);

        // Save changes
        Board updatedBoard = boardRepository.save(board);
        log.info("Board updated successfully with ID: {}", id);

        return boardMapper.toResponse(updatedBoard);
    }

    /**
     * Delete a board by ID
     * All pictograms will be deleted automatically (cascade)
     * @param id board ID
     * @throws ResourceNotFoundException if board doesn't exist
     */
    public void deleteBoard(Long id) {
        log.info("Deleting board with ID: {}", id);

        // Verify board exists
        if (!boardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Board not found with ID: " + id);
        }

        // Delete board
        boardRepository.deleteById(id);
        log.info("Board deleted successfully with ID: {}", id);
    }

    /**
     * Search boards by name (case-insensitive partial match)
     * @param name search term
     * @return list of matching boards
     */
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> searchBoardsByName(String name) {
        log.info("Searching boards with name containing: {}", name);
        List<Board> boards = boardRepository.findByNameContainingIgnoreCase(name);
        return boardMapper.toResponseList(boards);
    }

    /**
     * Get boards ordered by most recently created
     * @return list of boards
     */
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> getBoardsOrderedByNewest() {
        log.info("Fetching boards ordered by creation date");
        List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();
        return boardMapper.toResponseList(boards);
    }

    /**
     * Get boards ordered by most recently updated
     * @return list of boards
     */
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> getBoardsOrderedByRecentlyUpdated() {
        log.info("Fetching boards ordered by update date");
        List<Board> boards = boardRepository.findAllByOrderByUpdatedAtDesc();
        return boardMapper.toResponseList(boards);
    }

    /**
     * Get boards that have at least one pictogram
     * @return list of non-empty boards
     */
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> getBoardsWithPictograms() {
        log.info("Fetching boards with pictograms");
        List<Board> boards = boardRepository.findBoardsWithPictograms();
        return boardMapper.toResponseList(boards);
    }

    /**
     * Get empty boards (no pictograms)
     * @return list of empty boards
     */
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> getEmptyBoards() {
        log.info("Fetching empty boards");
        List<Board> boards = boardRepository.findEmptyBoards();
        return boardMapper.toResponseList(boards);
    }
    /**
     * Get boards by difficulty level
     * @param level difficulty level (1-3)
     * @return list of boards
     */
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> getBoardsByLevel(Integer level) {
        log.info("Fetching boards for level: {}", level);

        if (level < 1 || level > 3) {
            throw new IllegalArgumentException("Level must be between 1 and 3");
        }

        List<Board> boards = boardRepository.findByLevelOrderByCreatedAtDesc(level);
        return boardMapper.toResponseList(boards);
    }

    /**
     * Get predefined/template boards
     * @return list of predefined boards
     */
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> getPredefinedBoards() {
        log.info("Fetching predefined boards");
        List<Board> boards = boardRepository.findByIsPredefined(true);
        return boardMapper.toResponseList(boards);
    }

    /**
     * Check if a board exists
     * @param id board ID
     * @return true if exists
     */
    @Transactional(readOnly = true)
    public boolean boardExists(Long id) {
        return boardRepository.existsById(id);
    }

    // ============= HELPER METHODS =============

    /**
     * Validates that board can be resized without losing pictograms
     * Throws exception if any pictogram would be outside the new dimensions
     */
    private void validateBoardResize(Board board, Integer newRows, Integer newColumns) {
        for (BoardPictogram pictogram : board.getPictograms()) {
            if (pictogram.getPositionX() >= newColumns || pictogram.getPositionY() >= newRows) {
                throw new IllegalArgumentException(
                        String.format("Cannot resize board: pictogram '%s' at position (%d, %d) would be outside new dimensions (%dx%d)",
                                pictogram.getText(),
                                pictogram.getPositionX(),
                                pictogram.getPositionY(),
                                newColumns,
                                newRows
                        )
                );
            }
        }
    }





}
