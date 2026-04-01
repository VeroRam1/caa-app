package com.caa.backend.controller;

import com.caa.backend.dto.BoardRequestsDTOS.CreateBoardRequestDTO;
import com.caa.backend.dto.BoardRequestsDTOS.UpdateBoardRequestDTO;
import com.caa.backend.dto.ResponseDTOs.APIResponseDTO;
import com.caa.backend.dto.ResponseDTOs.BoardResponseDTO;
import com.caa.backend.service.BoardService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
@Tag(name="Boards", description="Board management API")
public class BoardController {
    private final BoardService boardService;

    // Get all boards

    @GetMapping
    @Operation(summary="Get all boards", description="List of all boards without pictograms")
    public ResponseEntity<List<BoardResponseDTO>> getAllBoards(){
        List<BoardResponseDTO> boards = boardService.getAllBoards();
        return ResponseEntity.ok(boards);
    }

    // Get board by ID

    @GetMapping("/{id}")
    @Operation(summary="Get all boards", description="List of all boards without pictograms")
    public ResponseEntity<BoardResponseDTO> getBoardById(@Parameter(description = "Board ID") @PathVariable Long id){
        BoardResponseDTO board = boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

    // Create new board
    @PostMapping
    @Operation(summary = "Create a new board", description = "Create a new communication board")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Board created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<APIResponseDTO<BoardResponseDTO>> createBoard(
            @Valid @RequestBody CreateBoardRequestDTO request){
        log.info("POST /api/boards - Creating new board: {}", request.getName());
        BoardResponseDTO createdBoard = boardService.createBoard(request);
        APIResponseDTO<BoardResponseDTO> response = APIResponseDTO.success("BoardCreatedSuccessfully", createdBoard);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Update an existing board
    @PutMapping("/{id}")
    @Operation(summary = "Update a board", description = "Update an existing board's information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Board updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<APIResponseDTO<BoardResponseDTO>> updateBoard(
            @Parameter(description = "Board ID") @PathVariable Long id,
            @Valid @RequestBody UpdateBoardRequestDTO request){
        log.info("PUT /api/boards/{} - Updating board", id);
        BoardResponseDTO updatedBoard = boardService.updateBoard(id, request);

        APIResponseDTO<BoardResponseDTO> response = APIResponseDTO.success( "Board updated successfully", updatedBoard);
        return ResponseEntity.ok(response);

    }

    // Update an existing board
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a board", description = "Delete a board and all its pictograms")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Board deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found")
    })
    public  ResponseEntity<APIResponseDTO<Void>> deleteBoard(
            @Parameter(description = "Board ID") @PathVariable Long id){
        log.info("DELETE /api/boards/{} - Deleting board", id);
        boardService.deleteBoard(id);
        APIResponseDTO<Void> response = APIResponseDTO.success("Board deleted successfully");
        return ResponseEntity.ok(response);

    }

    //  Search boards by name
    @GetMapping("/search")
    @Operation(summary = "Search boards", description = "Search boards by name")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Board deleted successfully"),
    })
    public ResponseEntity<List<BoardResponseDTO>> searchBoards(
            @Parameter(description = "Search term") @RequestParam String name){
        log.info("GET /api/boards/search?name={} - Searching boards", name);
        List<BoardResponseDTO> boards = boardService.searchBoardsByName(name);
        return ResponseEntity.ok(boards);
    }

    // Get boards ordered by creation date (newest first)
    @GetMapping("/recent")
    @Operation(summary = "Get recent boards", description = "Get boards ordered by creation date (newest first)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    public ResponseEntity<List<BoardResponseDTO>> getRecentBoards() {
        log.info("GET /api/boards/recent - Fetching recent boards");
        List<BoardResponseDTO> boards = boardService.getBoardsOrderedByNewest();
        return ResponseEntity.ok(boards);
    }

    // Get boards ordered by update date (most recently updated first)

    @GetMapping("/updated")
    @Operation(summary = "Get recently updated boards", description = "Get boards ordered by update date")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    public ResponseEntity<List<BoardResponseDTO>> getUpdatedBoards() {
        log.info("GET /api/boards/updated - Fetching recently updated boards");
        List<BoardResponseDTO> boards = boardService.getBoardsOrderedByRecentlyUpdated();
        return ResponseEntity.ok(boards);
    }

    // Get boards that have pictograms
    @GetMapping("/with-pictograms")
    @Operation(summary = "Get boards with pictograms", description = "Get only boards that contain at least one pictogram")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    public ResponseEntity<List<BoardResponseDTO>> getBoardsWithPictograms() {
        log.info("GET /api/boards/with-pictograms - Fetching boards with pictograms");
        List<BoardResponseDTO> boards = boardService.getBoardsWithPictograms();
        return ResponseEntity.ok(boards);
    }

    // Get empty boards (no pictograms)

    @GetMapping("/empty")
    @Operation(summary = "Get empty boards", description = "Get boards without any pictograms")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    public ResponseEntity<List<BoardResponseDTO>> getEmptyBoards() {
        log.info("GET /api/boards/empty - Fetching empty boards");
        List<BoardResponseDTO> boards = boardService.getEmptyBoards();
        return ResponseEntity.ok(boards);
    }

    /**
     * Get boards by level
     * GET /api/boards/level/{level}
     */
    @GetMapping("/level/{level}")
    @Operation(summary = "Get boards by level",
            description = "Get all boards for a specific difficulty level (1=Basic, 2=Intermediate, 3=Advanced)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    public ResponseEntity<List<BoardResponseDTO>> getBoardsByLevel(
            @Parameter(description = "Difficulty level (1-3)") @PathVariable Integer level) {
        log.info("GET /api/boards/level/{} - Fetching boards for level", level);
        List<BoardResponseDTO> boards = boardService.getBoardsByLevel(level);
        return ResponseEntity.ok(boards);
    }

    // Get predefined/template boards
    @GetMapping("/predefined")
    @Operation(summary = "Get predefined boards", description = "Get all predefined/template boards")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    public ResponseEntity<List<BoardResponseDTO>> getPredefinedBoards() {
        log.info("GET /api/boards/predefined - Fetching predefined boards");
        List<BoardResponseDTO> boards = boardService.getPredefinedBoards();
        return ResponseEntity.ok(boards);
    }


    // Check if a board exists
    @GetMapping("/{id}/exists")
    @Operation(summary = "Check if board exists", description = "Verify if a board exists by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Check completed")
    })
    public ResponseEntity<APIResponseDTO<Boolean>> boardExists(
            @Parameter(description = "Board ID") @PathVariable Long id) {
        log.info("GET /api/boards/{}/exists - Checking if board exists", id);
        boolean exists = boardService.boardExists(id);

        APIResponseDTO<Boolean> response = APIResponseDTO.success(
                exists ? "Board exists" : "Board does not exist",
                exists
        );

        return ResponseEntity.ok(response);
    }

}
