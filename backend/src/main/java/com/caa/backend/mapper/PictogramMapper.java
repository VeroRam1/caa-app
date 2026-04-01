package com.caa.backend.mapper;

import com.caa.backend.dto.PictogramRequestsDTOS.AddPictogramRequest;
import com.caa.backend.dto.PictogramRequestsDTOS.UpdatePictogramRequestDTO;
import com.caa.backend.dto.ResponseDTOs.PictogramResponseDTO;
import com.caa.backend.model.Board;
import com.caa.backend.model.BoardPictogram;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Pictogram entity and DTOs
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PictogramMapper {
    /**
     * Convert AddPictogramRequest to BoardPictogram entity
     * Used when adding a new pictogram to a board
     */
    @Mapping(target = "id", ignore = true)  // ID is auto-generated
    @Mapping(target = "board", ignore = true)  // Set manually in service
    @Mapping(source = "backgroundColor", target = "backgroundColor", defaultValue = "#FFFFFF")
    BoardPictogram toEntity(AddPictogramRequest request);

    /**
     * Convert AddPictogramRequest to BoardPictogram with board
     * Convenience method that includes board reference
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "request.backgroundColor", target = "backgroundColor", defaultValue = "#FFFFFF")
    @Mapping(source = "board", target = "board")
    BoardPictogram toEntity(AddPictogramRequest request, Board board);

    /**
     * Update existing BoardPictogram entity from UpdatePictogramRequest
     * Only updates non-null fields from request
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "board", ignore = true)
    @Mapping(target = "pictogramId", ignore = true)
    @Mapping(target = "pictogramUrl", ignore = true)
    void updateEntityFromRequest(UpdatePictogramRequestDTO request, @MappingTarget BoardPictogram pictogram);

    /**
     * Convert BoardPictogram entity to PictogramResponse DTO
     */
    PictogramResponseDTO toResponse(BoardPictogram pictogram);

    /**
     * Convert list of BoardPictogram entities to list of PictogramResponse DTOs
     */
    List<PictogramResponseDTO> toResponseList(List<BoardPictogram> pictograms);

    /**
     * After mapping hook to set default background color if null
     */
    @AfterMapping
    default void afterMappingToEntity(@MappingTarget BoardPictogram pictogram) {
        if (pictogram.getBackgroundColor() == null) {
            pictogram.setBackgroundColor("#FFFFFF");
        }
    }

    /**
     * After mapping hook for response to ensure all fields are set
     */
    @AfterMapping
    default void afterMappingToResponse(@MappingTarget PictogramResponseDTO response, BoardPictogram pictogram) {
        // Ensure background color is never null
        if (response.getBackgroundColor() == null) {
            response.setBackgroundColor("#FFFFFF");
        }
    }
}
