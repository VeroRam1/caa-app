package com.caa.backend.mapper;

import com.caa.backend.dto.ResponseDTOs.AuthResponseDTO;
import com.caa.backend.model.Tutor;
import org.mapstruct.*;

/**
 * MapStruct mapper for Tutor entity and DTOs
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TutorMapper {
    /**
     * Convert Tutor entity to AuthResponseDTO
     * The JWT token is not present in the entity — it must be set manually after mapping.
     * Used after both registration and login in TutorService:
     *   AuthResponseDTO response = tutorMapper.toAuthResponse(tutor);
     *   response.setToken(jwtUtil.generateToken(userDetails));
     */
    @Mapping(target = "tutorId", source = "id")
    @Mapping(target = "token", ignore = true)   // Set manually in service after mapping
    AuthResponseDTO toAuthResponse(Tutor tutor);
}
