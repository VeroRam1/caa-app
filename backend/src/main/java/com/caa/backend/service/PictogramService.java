package com.caa.backend.service;

import com.caa.backend.dto.PictogramRequestsDTOS.AddPictogramRequest;
import com.caa.backend.dto.PictogramRequestsDTOS.UpdatePictogramRequestDTO;
import com.caa.backend.dto.ResponseDTOs.PictogramResponseDTO;
import com.caa.backend.exception.ResourceNotFoundException;
import com.caa.backend.mapper.PictogramMapper;
import com.caa.backend.model.Board;
import com.caa.backend.model.BoardPictogram;
import com.caa.backend.repository.BoardPictogramRepository;
import com.caa.backend.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Service for Pictogram business logic. Handles pictograms on boards
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PictogramService {
    private final BoardPictogramRepository pictogramRepository;
    private final BoardRepository boardRepository;
    private final PictogramMapper pictogramMapper;

    // Get all pictograms from a specific board
    @Transactional(readOnly = true)
    public List<PictogramResponseDTO> getPictogramsByBoardId(Long boardId) {
        log.info("Fetching pictograms for board ID: {}", boardId);

        // Verify board exists
        verifyBoardExists(boardId);

        // Get pictograms ordered by position (row by row, left to right)
        List<BoardPictogram> pictograms = pictogramRepository
                .findByBoardIdOrderByPositionYAscPositionXAsc(boardId);

        return pictogramMapper.toResponseList(pictograms);
    }

    // Get a specific pictogram by ID
    @Transactional(readOnly = true)
    public PictogramResponseDTO getPictogramById(Long pictogramId) {
        log.info("Fetching pictogram with ID: {}", pictogramId);

        BoardPictogram pictogram = pictogramRepository.findById(pictogramId)
                .orElseThrow(() -> new ResourceNotFoundException("Pictogram not found with ID: " + pictogramId));

        return pictogramMapper.toResponse(pictogram);
    }

    // Add a new pictogram to a board
    public PictogramResponseDTO addPictogramToBoard(Long boardId, AddPictogramRequest request) {
        log.info("Adding pictogram '{}' to board ID: {}", request.getText(), boardId);

        // Find the board
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with ID: " + boardId));

        // Validate position is within board bounds
        if (!board.isValidPosition(request.getPositionX(), request.getPositionY())) {
            throw new IllegalArgumentException(
                    String.format("Invalid position (%d, %d) for board size %dx%d",
                            request.getPositionX(), request.getPositionY(),
                            board.getColumns(), board.getRows())
            );
        }
        // Check if position is already occupied
        if (pictogramRepository.existsByBoardIdAndPositionXAndPositionY(
                boardId, request.getPositionX(), request.getPositionY())) {
            throw new IllegalArgumentException(
                    String.format("Position (%d, %d) is already occupied",
                            request.getPositionX(), request.getPositionY())
            );
        }

        // Convert DTO to entity using mapper
        BoardPictogram pictogram = pictogramMapper.toEntity(request, board);

        // Save pictogram
        BoardPictogram savedPictogram = pictogramRepository.save(pictogram);
        log.info("Pictogram added successfully with ID: {}", savedPictogram.getId());

        return pictogramMapper.toResponse(savedPictogram);
    }

    // Update a pictogram (move position)
    public PictogramResponseDTO updatePictogram(Long pictogramId, UpdatePictogramRequestDTO request) {
        log.info("Updating pictogram with ID: {}", pictogramId);

        // Find existing pictogram
        BoardPictogram pictogram = pictogramRepository.findById(pictogramId)
                .orElseThrow(() -> new ResourceNotFoundException("Pictogram not found with ID: " + pictogramId));

        Board board = pictogram.getBoard();

        // Validate position if it's being updated
        if (request.getPositionX() != null || request.getPositionY() != null) {
            Integer newX = request.getPositionX() != null ? request.getPositionX() : pictogram.getPositionX();
            Integer newY = request.getPositionY() != null ? request.getPositionY() : pictogram.getPositionY();

            // Validate new position is within bounds
            if (!board.isValidPosition(newX, newY)) {
                throw new IllegalArgumentException(
                        String.format("Invalid position (%d, %d) for board size %dx%d",
                                newX, newY, board.getColumns(), board.getRows())
                );
            }

            // Check if new position is occupied (by another pictogram)
            pictogramRepository.findByBoardIdAndPositionXAndPositionY(board.getId(), newX, newY)
                    .ifPresent(existingPictogram -> {
                        if (!existingPictogram.getId().equals(pictogramId)) {
                            throw new IllegalArgumentException(
                                    String.format("Position (%d, %d) is already occupied by pictogram '%s'",
                                            newX, newY, existingPictogram.getText())
                            );
                        }
                    });
        }

        // Update entity using mapper (only non-null fields)
        pictogramMapper.updateEntityFromRequest(request, pictogram);

        // Save changes
        BoardPictogram updatedPictogram = pictogramRepository.save(pictogram);
        log.info("Pictogram updated successfully with ID: {}", pictogramId);

        return pictogramMapper.toResponse(updatedPictogram);
    }

    // Delete a pictogram from a board
    public void deletePictogram(Long pictogramId) {
        log.info("Deleting pictogram with ID: {}", pictogramId);

        // Verify pictogram exists
        if (!pictogramRepository.existsById(pictogramId)) {
            throw new ResourceNotFoundException("Pictogram not found with ID: " + pictogramId);
        }

        // Delete pictogram
        pictogramRepository.deleteById(pictogramId);
        log.info("Pictogram deleted successfully with ID: {}", pictogramId);
    }

    // Delete all pictograms from a board
    public void deleteAllPictogramsFromBoard(Long boardId) {
        log.info("Deleting all pictograms from board ID: {}", boardId);

        // Verify board exists
        verifyBoardExists(boardId);

        // Delete all pictograms
        pictogramRepository.deleteByBoardId(boardId);
        log.info("All pictograms deleted from board ID: {}", boardId);
    }

    // Check if a position is occupied on a board
    @Transactional(readOnly = true)
    public boolean isPositionOccupied(Long boardId, Integer x, Integer y) {
        return pictogramRepository.existsByBoardIdAndPositionXAndPositionY(boardId, x, y);
    }

    // Get pictogram at a specific position
    @Transactional(readOnly = true)
    public PictogramResponseDTO getPictogramAtPosition(Long boardId, Integer x, Integer y) {
        log.info("Fetching pictogram at position ({}, {}) on board ID: {}", x, y, boardId);

        return pictogramRepository.findByBoardIdAndPositionXAndPositionY(boardId, x, y)
                .map(pictogramMapper::toResponse)
                .orElse(null);
    }

    // Count pictograms on a board
    @Transactional(readOnly = true)
    public Long countPictogramsByBoardId(Long boardId) {
        return pictogramRepository.countByBoardId(boardId);
    }

    // Search pictograms by text across all boards
    @Transactional(readOnly = true)
    public List<PictogramResponseDTO> searchPictogramsByText(String text) {
        log.info("Searching pictograms with text containing: {}", text);

        List<BoardPictogram> pictograms = pictogramRepository.findByTextContainingIgnoreCase(text);

        return pictogramMapper.toResponseList(pictograms);
    }

    // Verify that a board exists
    private void verifyBoardExists(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new ResourceNotFoundException("Board not found with ID: " + boardId);
        }
    }
}
