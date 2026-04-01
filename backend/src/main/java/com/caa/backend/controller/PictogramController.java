package com.caa.backend.controller;

import com.caa.backend.dto.PictogramRequestsDTOS.AddPictogramRequest;
import com.caa.backend.dto.PictogramRequestsDTOS.UpdatePictogramRequestDTO;
import com.caa.backend.dto.ResponseDTOs.APIResponseDTO;
import com.caa.backend.dto.ResponseDTOs.PictogramResponseDTO;
import com.caa.backend.service.PictogramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/{boardId}/pictograms")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pictograms", description = "Pictogram management API")
public class PictogramController {
    private final PictogramService pictogramService;

    // Get all pictograms from a board
    @GetMapping
    @Operation(summary = "Get all pictograms from a board",
            description = "Retrieve all pictograms from a specific board, ordered by position")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found")
    })
    public ResponseEntity<List<PictogramResponseDTO>> getPictogramsByBoardId(
            @Parameter(description = "Board ID") @PathVariable Long boardId
    ){
        log.info("GET /api/boards/{}/pictograms - Fetching pictograms", boardId);
        List<PictogramResponseDTO> pictograms = pictogramService.getPictogramsByBoardId(boardId);
        return ResponseEntity.ok(pictograms);
    }

    // Get a specific pictogram by ID
    @GetMapping("/{pictogramId}")
    @Operation(summary = "Get pictogram by ID", description = "Retrieve a specific pictogram")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pictogram found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pictogram not found")
    })
    public ResponseEntity<PictogramResponseDTO> getPictogramById(
            @Parameter(description = "Board ID") @PathVariable Long boardId,
            @Parameter(description = "Pictogram ID") @PathVariable Long pictogramId) {
        log.info("GET /api/boards/{}/pictograms/{} - Fetching pictogram", boardId, pictogramId);
        PictogramResponseDTO pictogram = pictogramService.getPictogramById(pictogramId);
        return ResponseEntity.ok(pictogram);
    }

    /**
     * Add a pictogram to a board
     * POST /api/boards/{boardId}/pictograms
     */
    @PostMapping
    @Operation(summary = "Add pictogram to board",
            description = "Add a new pictogram to a specific position on the board")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Pictogram added successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or position occupied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found")
    })
    public ResponseEntity<APIResponseDTO<PictogramResponseDTO>> addPictogramToBoard(
            @Parameter(description = "Board ID") @PathVariable Long boardId,
            @Valid @RequestBody AddPictogramRequest request) {
        log.info("POST /api/boards/{}/pictograms - Adding pictogram at ({}, {})",
                boardId, request.getPositionX(), request.getPositionY());

        PictogramResponseDTO createdPictogram = pictogramService.addPictogramToBoard(boardId, request);

        APIResponseDTO<PictogramResponseDTO> response = APIResponseDTO.success(
                "Pictogram added successfully",
                createdPictogram
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update a pictogram (move position or change color)
     * PUT /api/boards/{boardId}/pictograms/{pictogramId}
     */
    @PutMapping("/{pictogramId}")
    @Operation(summary = "Update pictogram",
            description = "Update pictogram position, color, or text")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pictogram updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or position occupied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pictogram not found")
    })
    public ResponseEntity<APIResponseDTO<PictogramResponseDTO>> updatePictogram(
            @Parameter(description = "Board ID") @PathVariable Long boardId,
            @Parameter(description = "Pictogram ID") @PathVariable Long pictogramId,
            @Valid @RequestBody UpdatePictogramRequestDTO request) {
        log.info("PUT /api/boards/{}/pictograms/{} - Updating pictogram", boardId, pictogramId);

        PictogramResponseDTO updatedPictogram = pictogramService.updatePictogram(pictogramId, request);

        APIResponseDTO<PictogramResponseDTO> response = APIResponseDTO.success(
                "Pictogram updated successfully",
                updatedPictogram
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Delete a pictogram from a board
     * DELETE /api/boards/{boardId}/pictograms/{pictogramId}
     */
    @DeleteMapping("/{pictogramId}")
    @Operation(summary = "Delete pictogram", description = "Remove a pictogram from the board")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pictogram deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pictogram not found")
    })
    public ResponseEntity<APIResponseDTO<Void>> deletePictogram(
            @Parameter(description = "Board ID") @PathVariable Long boardId,
            @Parameter(description = "Pictogram ID") @PathVariable Long pictogramId) {
        log.info("DELETE /api/boards/{}/pictograms/{} - Deleting pictogram", boardId, pictogramId);

        pictogramService.deletePictogram(pictogramId);

        APIResponseDTO<Void> response = APIResponseDTO.success("Pictogram deleted successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Delete all pictograms from a board
     * DELETE /api/boards/{boardId}/pictograms
     */
    @DeleteMapping
    @Operation(summary = "Delete all pictograms",
            description = "Remove all pictograms from the board")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "All pictograms deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found")
    })
    public ResponseEntity<APIResponseDTO<Void>> deleteAllPictogramsFromBoard(
            @Parameter(description = "Board ID") @PathVariable Long boardId) {
        log.info("DELETE /api/boards/{}/pictograms - Deleting all pictograms", boardId);

        pictogramService.deleteAllPictogramsFromBoard(boardId);

        APIResponseDTO<Void> response = APIResponseDTO.success("All pictograms deleted successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Get pictogram at a specific position
     * GET /api/boards/{boardId}/pictograms/position?x=0&y=0
     */
    @GetMapping("/position")
    @Operation(summary = "Get pictogram at position",
            description = "Retrieve the pictogram at a specific grid position")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Position checked"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found")
    })
    public ResponseEntity<APIResponseDTO<PictogramResponseDTO>> getPictogramAtPosition(
            @Parameter(description = "Board ID") @PathVariable Long boardId,
            @Parameter(description = "X position (column)") @RequestParam Integer x,
            @Parameter(description = "Y position (row)") @RequestParam Integer y) {
        log.info("GET /api/boards/{}/pictograms/position?x={}&y={} - Checking position", boardId, x, y);

        PictogramResponseDTO pictogram = pictogramService.getPictogramAtPosition(boardId, x, y);

        if (pictogram != null) {
            APIResponseDTO<PictogramResponseDTO> response = APIResponseDTO.success(
                    "Pictogram found at position",
                    pictogram
            );
            return ResponseEntity.ok(response);
        } else {
            APIResponseDTO<PictogramResponseDTO> response = APIResponseDTO.success(
                    "No pictogram at this position",
                    null
            );
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Check if a position is occupied
     * GET /api/boards/{boardId}/pictograms/position/occupied?x=0&y=0
     */
    @GetMapping("/position/occupied")
    @Operation(summary = "Check if position is occupied",
            description = "Verify if a position on the board has a pictogram")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Check completed")
    })
    public ResponseEntity<APIResponseDTO<Boolean>> isPositionOccupied(
            @Parameter(description = "Board ID") @PathVariable Long boardId,
            @Parameter(description = "X position (column)") @RequestParam Integer x,
            @Parameter(description = "Y position (row)") @RequestParam Integer y) {
        log.info("GET /api/boards/{}/pictograms/position/occupied?x={}&y={}", boardId, x, y);

        boolean occupied = pictogramService.isPositionOccupied(boardId, x, y);

        APIResponseDTO<Boolean> response = APIResponseDTO.success(
                occupied ? "Position is occupied" : "Position is free",
                occupied
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Count pictograms on a board
     * GET /api/boards/{boardId}/pictograms/count
     */
    @GetMapping("/count")
    @Operation(summary = "Count pictograms",
            description = "Get the total number of pictograms on the board")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Count retrieved")
    })
    public ResponseEntity<APIResponseDTO<Long>> countPictogramsByBoardId(
            @Parameter(description = "Board ID") @PathVariable Long boardId) {
        log.info("GET /api/boards/{}/pictograms/count - Counting pictograms", boardId);

        Long count = pictogramService.countPictogramsByBoardId(boardId);

        APIResponseDTO<Long> response = APIResponseDTO.success(
                "Pictogram count retrieved",
                count
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Search pictograms by text across all boards
     * GET /api/pictograms/search?text=casa
     */
    @GetMapping("/search")
    @Operation(summary = "Search pictograms by text",
            description = "Search pictograms containing specific text across all boards")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search completed")
    })
    public ResponseEntity<List<PictogramResponseDTO>> searchPictogramsByText(
            @Parameter(description = "Search term") @RequestParam String text) {
        log.info("GET /api/pictograms/search?text={}", text);

        List<PictogramResponseDTO> pictograms = pictogramService.searchPictogramsByText(text);

        return ResponseEntity.ok(pictograms);
    }
}
