package com.caa.backend.dto.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// DTO for ARASAAC API pictogram response. Maps the structure returned by ARASAAC API
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArasaacPictogramResponseDTO {

    @JsonProperty("_id")
    private Integer id;

    @JsonProperty("keywords")
    private Object keywords;
    private List<String> categories;
    private List<String> tags;
    private String author;
    private Integer type;
    private Boolean hasAnimation;
    private Boolean schematic;
    private Boolean color;
    private Boolean noBackground;
    private transient List<String> keywordsEs;
    public String getPrimaryKeyword() {
        List<String> kw = getKeywordsEs();
        if (kw != null && !kw.isEmpty()) {
            return kw.get(0);
        }
        return "sin nombre";
    }

    // Helper method to get all keywords in Spanish as comma-separated string
    public String getAllKeywords() {
        List<String> kw = getKeywordsEs();
        if (kw != null && !kw.isEmpty()) {
            return String.join(", ", kw);
        }
        return "";
    }

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

    // Generate the URL for the pictogram image
    public String getPictogramUrl(int size, boolean withBackground) {
        return String.format(
                "https://api.arasaac.org/v1/pictograms/%d?download=false&plural=false&color=true&backgroundColor=%s&size=%d",
                id, withBackground ? "true" : "false", size
        );
    }

    public String getDefaultPictogramUrl() {
        return getPictogramUrl(300, true);
    }
}