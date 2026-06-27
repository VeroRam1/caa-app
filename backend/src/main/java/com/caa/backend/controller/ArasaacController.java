package com.caa.backend.controller;

import com.caa.backend.dto.ResponseDTOs.ArasaacPictogramResponseDTO;
import com.caa.backend.service.ArasaacService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for ARASAAC integration
 * Base path: /api/arasaac
 */
@RestController
@RequestMapping("/api/arasaac")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ARASAAC", description = "ARASAAC pictogram search and retrieval API")
public class ArasaacController {

    private final ArasaacService arasaacService;

    // Search pictograms by keyword. Ex. GET /api/arasaac/search?keyword=casa
    @GetMapping("/search")
    @Operation(summary = "Search pictograms",
            description = "Search pictograms in ARASAAC by keyword")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search completed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid keyword")
    })
    public ResponseEntity<List<ArasaacPictogramResponseDTO>> searchPictograms(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Language code (default: es)") @RequestParam(required = false) String language) {
        log.info("GET /api/arasaac/search?keyword={}&language={}", keyword, language);

        List<ArasaacPictogramResponseDTO> pictograms = arasaacService.searchPictograms(keyword, language);

        return ResponseEntity.ok(pictograms);
    }

    @GetMapping("/pictograms/{id}")
    @Operation(summary = "Get pictogram by ID",
            description = "Retrieve detailed information about a specific pictogram")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pictogram found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pictogram not found")
    })
    public ResponseEntity<ArasaacPictogramResponseDTO> getPictogramById(
            @Parameter(description = "Pictogram ID") @PathVariable Integer id,
            @Parameter(description = "Language code (default: es)") @RequestParam(required = false) String language) {
        log.info("GET /api/arasaac/pictograms/{}?language={}", id, language);

        ArasaacPictogramResponseDTO pictogram = arasaacService.getPictogramById(id, language);

        return ResponseEntity.ok(pictogram);
    }

    @GetMapping("/popular")
    @Operation(summary = "Get popular pictograms",
            description = "Get a list of commonly used pictograms for quick access")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List retrieved")
    })
    public ResponseEntity<List<ArasaacPictogramResponseDTO>> getPopularPictograms() {
        log.info("GET /api/arasaac/popular - Fetching popular pictograms");

        List<ArasaacPictogramResponseDTO> pictograms = arasaacService.getPopularPictograms();

        return ResponseEntity.ok(pictograms);
    }

    @GetMapping("/pictograms/{id}/url")
    @Operation(summary = "Get pictogram image URL",
            description = "Generate the URL for a pictogram image with specific options")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "URL generated")
    })
    public ResponseEntity<String> getPictogramUrl(
            @Parameter(description = "Pictogram ID") @PathVariable Integer id,
            @Parameter(description = "Image size (300, 500, 2500)") @RequestParam(defaultValue = "300") int size,
            @Parameter(description = "Include background") @RequestParam(defaultValue = "true") boolean background) {
        log.info("GET /api/arasaac/pictograms/{}/url?size={}&background={}", id, size, background);

        String url = arasaacService.getPictogramUrl(id, size, background);

        return ResponseEntity.ok(url);
    }
}