package com.caa.backend.controller;

import com.caa.backend.dto.BoardRequestsDTOS.CreateBoardRequestDTO;
import com.caa.backend.dto.BoardRequestsDTOS.ResizeBoardRequestDTO;
import com.caa.backend.dto.BoardRequestsDTOS.UpdateBoardRequestDTO;
import com.caa.backend.dto.BoardRequestsDTOS.UpdateBoardPictogramsRequestDTO;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    // Get all boards created by the logged tutor
    @GetMapping("/myBoards")
    @Operation(summary = "Get tutor's boards",
            description = "Returns all boards owned by the authenticated tutor")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<BoardResponseDTO>> getMyBoards(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("GET /api/boards/myBoards - Fetching boards for tutor: {}", userDetails.getUsername());
        List<BoardResponseDTO> boards = boardService.getBoardsByOwner(userDetails.getUsername());
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
    public ResponseEntity<APIResponseDTO<BoardResponseDTO>> createBoard(
            @Valid @RequestBody CreateBoardRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/boards - Creating new board: {}", request.getName());
        BoardResponseDTO createdBoard = boardService.createBoard(request, userDetails.getUsername());
        APIResponseDTO<BoardResponseDTO> response = APIResponseDTO.success("Board created successfully", createdBoard);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Creates a copy of a predefined board so the tutor can edit it
    @PostMapping("/{id}/copy")
    @Operation(summary = "Copy a board",
            description = "Creates an editable copy of a predefined board for the tutor")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Board copied successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponseDTO<BoardResponseDTO>> copyBoard(
            @Parameter(description = "Board ID to copy") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/boards/{}/copy - Copying board for tutor: {}",
                id, userDetails.getUsername());
        BoardResponseDTO copiedBoard = boardService.copyBoard(id, userDetails.getUsername());
        APIResponseDTO<BoardResponseDTO> response =
                APIResponseDTO.success("Tablero copiado correctamente", copiedBoard);
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

    @PutMapping("/{id}/pictograms")
    @Operation(summary = "Update board pictograms",
            description = "Replaces the pictogram layout of a board. Only the owner can do this.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pictograms updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not the board owner"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Board not found")
    })
    public ResponseEntity<APIResponseDTO<BoardResponseDTO>> updateBoardPictograms(
            @Parameter(description = "Board ID") @PathVariable Long id,
            @Valid @RequestBody UpdateBoardPictogramsRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /api/boards/{}/pictograms - Updating pictograms for tutor: {}",
                id, userDetails.getUsername());
        BoardResponseDTO updatedBoard =
                boardService.updateBoardPictograms(id, request, userDetails.getUsername());
        APIResponseDTO<BoardResponseDTO> response =
                APIResponseDTO.success("Pictogramas actualizados correctamente", updatedBoard);
        return ResponseEntity.ok(response);
    }

    // Changes the rows and columns of a board and validates that no pictograms fall outside the new dimensions.
    @PatchMapping("/{id}/resize")
    @Operation(summary = "Resize a board",
            description = "Changes the grid size. Fails if pictograms would fall outside new dimensions.")
    public ResponseEntity<APIResponseDTO<BoardResponseDTO>> resizeBoard(
            @PathVariable Long id,
            @Valid @RequestBody ResizeBoardRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PATCH /api/boards/{}/resize - Resizing to {}x{}", id, request.getRows(), request.getColumns());
        BoardResponseDTO resizedBoard =
                boardService.resizeBoard(id, request, userDetails.getUsername());
        APIResponseDTO<BoardResponseDTO> response =
                APIResponseDTO.success("Tablero redimensionado correctamente", resizedBoard);
        return ResponseEntity.ok(response);
    }

    // Deletes an existing board
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

    // Get boards by level
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
