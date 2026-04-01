package com.caa.backend.dto.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for ARASAAC API pictogram response
 * Maps the structure returned by ARASAAC API
 * Designed for children's AAC application
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArasaacPictogramResponseDTO {

    /**
     * Pictogram ID in ARASAAC
     */
    @JsonProperty("_id")
    private Integer id;

    /**
     * Keywords - this is a nested object in ARASAAC
     * We'll extract it manually in the service
     */
    @JsonProperty("keywords")
    private Object keywords;  // Temporarily as Object to avoid parsing errors

    /**
     * Categories
     */
    private List<String> categories;

    /**
     * Tags
     */
    private List<String> tags;

    /**
     * Author information
     */
    private String author;

    /**
     * Type of pictogram
     */
    private Integer type;

    /**
     * Whether it has animation
     */
    private Boolean hasAnimation;

    /**
     * Whether schematic representation exists
     */
    private Boolean schematic;

    /**
     * Whether it has color version
     */
    private Boolean color;

    /**
     * Whether it has no-background version
     */
    private Boolean noBackground;

    /**
     * Cached keywords in Spanish (extracted from the keywords object)
     */
    private transient List<String> keywordsEs;

    /**
     * Helper method to get the primary keyword in Spanish
     * @return first Spanish keyword or "sin nombre"
     */
    public String getPrimaryKeyword() {
        List<String> kw = getKeywordsEs();
        if (kw != null && !kw.isEmpty()) {
            return kw.get(0);
        }
        return "sin nombre";
    }

    /**
     * Helper method to get all keywords in Spanish as comma-separated string
     * @return keywords joined
     */
    public String getAllKeywords() {
        List<String> kw = getKeywordsEs();
        if (kw != null && !kw.isEmpty()) {
            return String.join(", ", kw);
        }
        return "";
    }

    /**
     * Extract Spanish keywords from the keywords object
     * @return list of Spanish keywords
     */
    @SuppressWarnings("unchecked")
    public List<String> getKeywordsEs() {
        if (keywordsEs != null) {
            return keywordsEs;
        }

        keywordsEs = new ArrayList<>();

        if (keywords == null) {
            return keywordsEs;
        }

        try {
            if (keywords instanceof List) {
                List<Object> keywordList = (List<Object>) keywords;
                for (Object item : keywordList) {
                    if (item instanceof java.util.Map) {
                        java.util.Map<String, Object> kwMap = (java.util.Map<String, Object>) item;
                        Object kw = kwMap.get("keyword");
                        if (kw instanceof String) {
                            keywordsEs.add((String) kw);
                        }
                    } else if (item instanceof String) {
                        keywordsEs.add((String) item);
                    }
                }
            }
        } catch (Exception e) {
            // Si falla, devolver lista vacía
        }

        return keywordsEs;
    }

    /**
     * Generate the URL for the pictogram image
     * @param size image size (300, 500, 2500)
     * @param withBackground include background
     * @return full URL
     */
    public String getPictogramUrl(int size, boolean withBackground) {
        return String.format(
                "https://api.arasaac.org/v1/pictograms/%d?download=false&plural=false&color=true&backgroundColor=%s&size=%d",
                id, withBackground ? "true" : "false", size
        );
    }

    /**
     * Get default pictogram URL (300px, with background)
     * @return default URL
     */
    public String getDefaultPictogramUrl() {
        return getPictogramUrl(300, true);
    }
}