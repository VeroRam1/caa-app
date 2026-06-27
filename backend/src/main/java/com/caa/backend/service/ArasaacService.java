package com.caa.backend.service;


import com.caa.backend.dto.ResponseDTOs.ArasaacPictogramResponseDTO;
import com.caa.backend.dto.ResponseDTOs.ArasaacPictogramResponseDTO;
import com.caa.backend.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

// Service for integrating with ARASAAC API. Provides methods to search and retrieve pictograms
@Service
@RequiredArgsConstructor
@Slf4j
public class ArasaacService {

    private static final String ARASAAC_BASE_URL = "https://api.arasaac.org/v1";
    private static final String DEFAULT_LANGUAGE = "es";

    private final RestTemplate restTemplate;

    // Search pictograms by keyword. Results are cached to improve performance
    @Cacheable(value = "pictogramSearch", key = "#keyword + '_' + #language")
    public List<ArasaacPictogramResponseDTO> searchPictograms(String keyword, String language) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }

        String lang = language != null ? language : DEFAULT_LANGUAGE;
        log.info("Searching ARASAAC for keyword: '{}' in language: {}", keyword, lang);

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(ARASAAC_BASE_URL + "/pictograms/" + lang + "/search/" + keyword.trim())
                    .toUriString();

            ArasaacPictogramResponseDTO[] response = restTemplate.getForObject(url, ArasaacPictogramResponseDTO[].class);

            if (response == null || response.length == 0) {
                log.info("No pictograms found for keyword: '{}'", keyword);
                return List.of();
            }

            log.info("Found {} pictograms for keyword: '{}'", response.length, keyword);
            return Arrays.asList(response);

        } catch (Exception e) {
            log.error("Error searching pictograms in ARASAAC: {}", e.getMessage(), e);
            throw new RuntimeException("Error connecting to ARASAAC API: " + e.getMessage());
        }
    }

    // Search pictograms by keyword (default Spanish)
    public List<ArasaacPictogramResponseDTO> searchPictograms(String keyword) {
        return searchPictograms(keyword, DEFAULT_LANGUAGE);
    }

    // Get detailed information about a specific pictogram. Results are cached
    @Cacheable(value = "pictogramDetails", key = "#pictogramId + '_' + #language")
    public ArasaacPictogramResponseDTO getPictogramById(Integer pictogramId, String language) {
        if (pictogramId == null || pictogramId <= 0) {
            throw new BadRequestException("Invalid pictogram ID");
        }

        String lang = language != null ? language : DEFAULT_LANGUAGE;
        log.info("Fetching pictogram {} in language: {}", pictogramId, lang);

        try {
            String url = String.format("%s/pictograms/%s/%d", ARASAAC_BASE_URL, lang, pictogramId);

            ArasaacPictogramResponseDTO response = restTemplate.getForObject(url, ArasaacPictogramResponseDTO.class);

            if (response == null) {
                throw new RuntimeException("Pictogram not found in ARASAAC");
            }

            log.info("Pictogram {} fetched successfully", pictogramId);
            return response;

        } catch (Exception e) {
            log.error("Error fetching pictogram from ARASAAC: {}", e.getMessage(), e);
            throw new RuntimeException("Error connecting to ARASAAC API: " + e.getMessage());
        }
    }

    public ArasaacPictogramResponseDTO getPictogramById(Integer pictogramId) {
        return getPictogramById(pictogramId, DEFAULT_LANGUAGE);
    }

    public String getPictogramUrl(Integer pictogramId, int size, boolean withBackground) {
        return String.format(
                "https://api.arasaac.org/v1/pictograms/%d?download=false&plural=false&color=true&backgroundColor=%s&size=%d",
                pictogramId, withBackground ? "true" : "false", size
        );
    }

    public String getDefaultPictogramUrl(Integer pictogramId) {
        return getPictogramUrl(pictogramId, 300, true);
    }

    public List<Integer> getPopularPictogramIds() {
        // Common pictograms IDs in ARASAAC
        return List.of(
                2454,
                7841,
                5781,
                11367,
                34027,
                26061,
                8484,
                33633,
                2,
                5938,
                7873,
                5956
        );
    }

    public List<ArasaacPictogramResponseDTO> getPopularPictograms() {
        log.info("Fetching popular pictograms");
        return getPopularPictogramIds().stream()
                .map(this::getPictogramById)
                .toList();
    }
}