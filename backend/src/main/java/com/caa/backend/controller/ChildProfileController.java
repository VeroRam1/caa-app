package com.caa.backend.controller;

import com.caa.backend.dto.ChangeLevelRequestDTO;
import com.caa.backend.dto.ChildProfileRequestDTO;
import com.caa.backend.dto.ResponseDTOs.APIResponseDTO;
import com.caa.backend.dto.ResponseDTOs.ChildProfileResponseDTO;
import com.caa.backend.model.ChildProfile;
import com.caa.backend.service.ChildProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Tutor registration and login")
public class ChildProfileController {
    private final ChildProfileService childProfileService;

    @GetMapping
    @Operation(summary = "Get all child profiles", description = "Returns all children belonging to the authenticated tutor")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ChildProfileResponseDTO>> getAllProfiles(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        log.info("GET /api/child-profiles - Fetching all profiles for tutor: {}", userDetails.getUsername());
        List<ChildProfileResponseDTO> profiles = childProfileService.getAllProfiles(userDetails.getUsername());
        return ResponseEntity.ok(profiles);
    }

    // Get child profile by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get child profile by ID", description = "Returns a child profile with all its assigned boards")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Child profile not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ChildProfileResponseDTO> getProfileById(
            @Parameter(description = "Child profile ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("GET /api/child-profiles/{} - Fetching child profile", id);
        ChildProfileResponseDTO profile = childProfileService.getProfileById(id, userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    // Create new child profile
    @PostMapping
    @Operation(summary = "Create a child profile", description = "Creates a new child profile under the authenticated tutor's account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Child profile created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponseDTO<ChildProfileResponseDTO>> createProfile(
            @Valid @RequestBody ChildProfileRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/child-profiles - Creating profile '{}' for tutor: {}", request.getName(), userDetails.getUsername());
        ChildProfileResponseDTO createdProfile = childProfileService.createProfile(request, userDetails.getUsername());
        APIResponseDTO<ChildProfileResponseDTO> response = APIResponseDTO.success("Child profile created successfully", createdProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // Update child profile
    @PutMapping("/{id}")
    @Operation(summary = "Update a child profile", description = "Updates an existing child profile's information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Child profile updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Child profile not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponseDTO<ChildProfileResponseDTO>> updateProfile(
            @Parameter(description = "Child profile ID") @PathVariable Long id,
            @Valid @RequestBody ChildProfileRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /api/child-profiles/{} - Updating child profile", id);
        ChildProfileResponseDTO updatedProfile = childProfileService.updateProfile(id, request, userDetails.getUsername());
        APIResponseDTO<ChildProfileResponseDTO> response = APIResponseDTO.success("Child profile updated successfully", updatedProfile);
        return ResponseEntity.ok(response);
    }

    // Delete child profile
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a child profile", description = "Deletes a child profile and all its board assignments")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Child profile deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Child profile not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponseDTO<Void>> deleteProfile(
            @Parameter(description = "Child profile ID") @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /api/child-profiles/{} - Deleting child profile", id);
        childProfileService.deleteProfile(id, userDetails.getUsername());
        APIResponseDTO<Void> response = APIResponseDTO.success("Child profile deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Change communication level
    @PatchMapping("/{id}/level")
    @Operation(summary = "Change communication level", description = "Updates only the communication level (LEVEL_1, LEVEL_2, LEVEL_3) of a child profile")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Level updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Child profile not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid level"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponseDTO<ChildProfileResponseDTO>> changeLevel(
            @Parameter(description = "Child profile ID") @PathVariable Long id,
            @Valid @RequestBody ChangeLevelRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PATCH /api/child-profiles/{}/level - Changing level to {}", id, request.getLevel());
        ChildProfileResponseDTO updatedProfile = childProfileService.changeLevel(id, request, userDetails.getUsername());
        APIResponseDTO<ChildProfileResponseDTO> response = APIResponseDTO.success("Level updated successfully", updatedProfile);
        return ResponseEntity.ok(response);
    }

    // Assign board to child profile
    @PostMapping("/{profileId}/boards/{boardId}")
    @Operation(summary = "Assign a board", description = "Assigns an existing board to a child profile")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Board assigned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Child profile or board not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponseDTO<ChildProfileResponseDTO>> assignBoard(
            @Parameter(description = "Child profile ID") @PathVariable Long profileId,
            @Parameter(description = "Board ID") @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/child-profiles/{}/boards/{} - Assigning board", profileId, boardId);
        ChildProfileResponseDTO updatedProfile = childProfileService.assignBoard(profileId, boardId, userDetails.getUsername());
        APIResponseDTO<ChildProfileResponseDTO> response = APIResponseDTO.success("Board assigned successfully", updatedProfile);
        return ResponseEntity.ok(response);
    }

    // Remove board from child profile
    @DeleteMapping("/{profileId}/boards/{boardId}")
    @Operation(summary = "Remove a board", description = "Removes a board assignment from a child profile without deleting the board")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Board removed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Child profile or board not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<APIResponseDTO<ChildProfileResponseDTO>> removeBoard(
            @Parameter(description = "Child profile ID") @PathVariable Long profileId,
            @Parameter(description = "Board ID") @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /api/child-profiles/{}/boards/{} - Removing board", profileId, boardId);
        ChildProfileResponseDTO updatedProfile = childProfileService.removeBoard(profileId, boardId, userDetails.getUsername());
        APIResponseDTO<ChildProfileResponseDTO> response = APIResponseDTO.success("Board removed successfully", updatedProfile);
        return ResponseEntity.ok(response);
    }

}
