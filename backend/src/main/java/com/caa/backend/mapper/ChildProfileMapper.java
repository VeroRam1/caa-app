package com.caa.backend.mapper;

import com.caa.backend.dto.ChangeLevelRequestDTO;
import com.caa.backend.dto.ChildProfileRequestDTO;
import com.caa.backend.dto.ResponseDTOs.ChildProfileResponseDTO;
import com.caa.backend.model.ChildProfile;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

// MapStruct mapper for ChildProfile entity and DTOs. Automatically generates implementation at compile time
@Mapper(
        componentModel = "spring",
        uses = {BoardMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ChildProfileMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tutor", ignore = true)
    @Mapping(target = "assignedBoards", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "level", expression = "java(request.getLevelOrDefault())")
    ChildProfile toEntity(ChildProfileRequestDTO request);

    // Update existing ChildProfile entity from ChildProfileRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tutor", ignore = true)
    @Mapping(target = "assignedBoards", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(ChildProfileRequestDTO request, @MappingTarget ChildProfile childProfile);

    // Update only the level of an existing ChildProfile entity/
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    @Mapping(target = "tutor", ignore = true)
    @Mapping(target = "assignedBoards", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateLevelFromRequest(ChangeLevelRequestDTO request, @MappingTarget ChildProfile childProfile);

    // Convert ChildProfile entity to ChildProfileResponseDTO (without boards)
    @Mapping(target = "tutorId", source = "tutor.id")
    @Mapping(target = "boardCount", expression = "java(childProfile.getAssignedBoards().size())")
    @Mapping(target = "assignedBoards", qualifiedByName = "toResponseList")
    @Named("toResponse")
    ChildProfileResponseDTO toResponse(ChildProfile childProfile);

    // Convert ChildProfile entity to ChildProfileResponseDTO (with boards)
    @Mapping(target = "tutorId", source = "tutor.id")
    @Mapping(target = "boardCount", expression = "java(childProfile.getAssignedBoards().size())")
    @Named("toResponseWithBoards")
    ChildProfileResponseDTO toResponseWithBoards(ChildProfile childProfile);

    // Convert list of ChildProfile entities to list of ChildProfileResponseDTOs (without boards)
    @Named("toResponseList")
    default List<ChildProfileResponseDTO> toResponseList(List<ChildProfile> childProfiles) {
        if (childProfiles == null) {
            return null;
        }
        return childProfiles.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // After mapping hook to ensure data consistency
    @AfterMapping
    default void afterMapping(@MappingTarget ChildProfileResponseDTO response, ChildProfile childProfile) {
        // Ensure boardCount is set correctly
        if (response.getBoardCount() == null) {
            response.setBoardCount(childProfile.getAssignedBoards() != null ?
                    childProfile.getAssignedBoards().size() : 0);
        }
    }
}
