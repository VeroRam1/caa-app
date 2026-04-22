package com.caa.backend.service;

import com.caa.backend.dto.ChangeLevelRequestDTO;
import com.caa.backend.dto.ChildProfileRequestDTO;
import com.caa.backend.dto.ResponseDTOs.ChildProfileResponseDTO;
import com.caa.backend.exception.ResourceNotFoundException;
import com.caa.backend.mapper.ChildProfileMapper;
import com.caa.backend.model.Board;
import com.caa.backend.model.ChildProfile;
import com.caa.backend.model.Tutor;
import com.caa.backend.model.enums.Level;
import com.caa.backend.repository.BoardRepository;
import com.caa.backend.repository.ChildProfileRepository;
import com.caa.backend.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChildProfileService {
    private final ChildProfileRepository childProfileRepository;
    private final TutorRepository tutorRepository;
    private final BoardRepository boardRepository;
    private final ChildProfileMapper childProfileMapper;
    // Uploading images
    private final FileStorageService fileStorageService;

    // ============= QUERIES =============

    /**
     * Get all child profiles belonging to the authenticated tutor
     * @param tutorEmail email extracted from JWT in the controller
     * @return list of child profiles without board detail (for performance)
     */
    @Transactional(readOnly = true)
    public List<ChildProfileResponseDTO> getAllProfiles(String tutorEmail) {
        log.info("Fetching all child profiles for tutor: {}", tutorEmail);
        Tutor tutor = loadTutorByEmail(tutorEmail);
        List<ChildProfile> profiles = childProfileRepository.findByTutorId(tutor.getId());
        return childProfileMapper.toResponseList(profiles);
    }

    /**
     * Get a specific child profile by ID with all its assigned boards
     * @param id child profile ID
     * @param tutorEmail email extracted from JWT in the controller
     * @return child profile with boards
     * @throws ResourceNotFoundException if profile doesn't exist or doesn't belong to this tutor
     */
    @Transactional(readOnly = true)
    public ChildProfileResponseDTO getProfileById(Long id, String tutorEmail) {
        log.info("Fetching child profile with ID: {}", id);
        Tutor tutor = loadTutorByEmail(tutorEmail);

        // Usa el query con JOIN FETCH para cargar boards en una sola query
        ChildProfile profile = childProfileRepository.findByIdAndTutorIdWithBoards(id, tutor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Child profile not found with ID: " + id));

        return childProfileMapper.toResponseWithBoards(profile);
    }

    /**
     * Uploads a profile photo for a child profile.
     * Saves the file to disk, updates photoUrl in the DB,
     * and deletes the old photo if one existed.
     *
     * @param profileId  child profile ID
     * @param file       uploaded image file
     * @param tutorEmail authenticated tutor's email
     * @return updated child profile response
     */
    @Transactional
    public ChildProfileResponseDTO uploadPhoto(Long profileId, MultipartFile file, String tutorEmail) {
        log.info("Uploading photo for child profile ID: {}", profileId);
        Tutor tutor = loadTutorByEmail(tutorEmail);
        ChildProfile profile = loadProfileForTutor(profileId, tutor.getId());

        try {
            // Delete old photo from disk if it exists
            if (profile.getPhotoUrl() != null) {
                fileStorageService.deleteProfilePhoto(profile.getPhotoUrl());
            }

            // Save new photo and update URL in profile
            String photoUrl = fileStorageService.saveProfilePhoto(file);
            profile.setPhotoUrl(photoUrl);

            ChildProfile updated = childProfileRepository.save(profile);
            log.info("Photo uploaded successfully for child profile ID: {}", profileId);
            return childProfileMapper.toResponse(updated);

        } catch (IllegalArgumentException e) {
            throw e; // Re-throw validation errors (wrong file type)
        } catch (Exception e) {
            log.error("Error uploading photo for child profile ID: {}", profileId, e);
            throw new RuntimeException("Error al subir la foto. Inténtalo de nuevo.");
        }
    }

    @Transactional
    public ChildProfileResponseDTO createProfile(ChildProfileRequestDTO dto, String tutorEmail) {
        log.info("Creating new child profile '{}' for tutor: {}", dto.getName(), tutorEmail);
        Tutor tutor = loadTutorByEmail(tutorEmail);

        ChildProfile profile = childProfileMapper.toEntity(dto);
        tutor.addChildProfile(profile);

        // Auto-assign the default predefined board matching the profile's level
        int levelNumber = dto.getLevelOrDefault() == Level.LEVEL_1 ? 1 :
                dto.getLevelOrDefault() == Level.LEVEL_2 ? 2 : 3;

        boardRepository.findByIsPredefined(true).stream()
                .filter(b -> b.getLevel() == levelNumber &&
                        b.getName().toLowerCase().contains("general"))
                .findFirst()
                .ifPresent(profile::assignBoard);

        ChildProfile saved = childProfileRepository.save(profile);
        log.info("Child profile created with ID: {}", saved.getId());
        return childProfileMapper.toResponse(saved);
    }

    /**
     * Update an existing child profile
     * Only updates non-null fields from the request (name, birthDate, photoUrl, level)
     * Does NOT update tutor or assignedBoards — managed separately
     * @param id child profile ID
     * @param request update data
     * @param tutorEmail email extracted from JWT in the controller
     * @return updated child profile
     * @throws ResourceNotFoundException if profile doesn't exist or doesn't belong to this tutor
     */
    public ChildProfileResponseDTO updateProfile(Long id, ChildProfileRequestDTO request, String tutorEmail) {
        log.info("Updating child profile with ID: {}", id);
        Tutor tutor = loadTutorByEmail(tutorEmail);
        ChildProfile profile = loadProfileForTutor(id, tutor.getId());

        childProfileMapper.updateEntityFromRequest(request, profile);

        ChildProfile updatedProfile = childProfileRepository.save(profile);
        log.info("Child profile updated successfully with ID: {}", id);

        return childProfileMapper.toResponse(updatedProfile);
    }

    /**
     * Delete a child profile
     * Only the tutor who owns the profile can delete it
     * @param id child profile ID
     * @param tutorEmail email extracted from JWT in the controller
     * @throws ResourceNotFoundException if profile doesn't exist or doesn't belong to this tutor
     */
    public void deleteProfile(Long id, String tutorEmail) {
        log.info("Deleting child profile with ID: {}", id);
        Tutor tutor = loadTutorByEmail(tutorEmail);
        ChildProfile profile = loadProfileForTutor(id, tutor.getId());

        tutor.removeChildProfile(profile); // Clears bidirectional relationship
        childProfileRepository.delete(profile);
        log.info("Child profile deleted successfully with ID: {}", id);
    }

    /**
     * Change the communication level of a child profile independently
     * Separated from updateProfile so the tutor can update level without resending full data
     * @param id child profile ID
     * @param request new level
     * @param tutorEmail email extracted from JWT in the controller
     * @return updated child profile
     * @throws ResourceNotFoundException if profile doesn't exist or doesn't belong to this tutor
     */
    public ChildProfileResponseDTO changeLevel(Long id, ChangeLevelRequestDTO request, String tutorEmail) {
        log.info("Changing level of child profile ID: {} to {}", id, request.getLevel());
        Tutor tutor = loadTutorByEmail(tutorEmail);
        ChildProfile profile = loadProfileForTutor(id, tutor.getId());

        childProfileMapper.updateLevelFromRequest(request, profile);

        ChildProfile updatedProfile = childProfileRepository.save(profile);
        log.info("Level updated successfully for child profile ID: {}", id);

        return childProfileMapper.toResponse(updatedProfile);
    }

    // ============= BOARD ASSIGNMENT =============

    /**
     * Assign an existing board to a child profile
     * Avoids duplicates — assigning the same board twice has no effect
     * @param profileId child profile ID
     * @param boardId ID of the board to assign
     * @param tutorEmail email extracted from JWT in the controller
     * @return updated child profile with boards
     * @throws ResourceNotFoundException if profile or board don't exist
     */
    public ChildProfileResponseDTO assignBoard(Long profileId, Long boardId, String tutorEmail) {
        log.info("Assigning board ID: {} to child profile ID: {}", boardId, profileId);
        Tutor tutor = loadTutorByEmail(tutorEmail);
        ChildProfile profile = loadProfileForTutor(profileId, tutor.getId());

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with ID: " + boardId));

        profile.assignBoard(board); // Avoids duplicates (defined in ChildProfile entity)

        ChildProfile updatedProfile = childProfileRepository.save(profile);
        log.info("Board ID: {} assigned successfully to child profile ID: {}", boardId, profileId);

        return childProfileMapper.toResponseWithBoards(updatedProfile);
    }

    /**
     * Remove a board from a child profile
     * Does not delete the board itself — only removes the assignment
     * @param profileId child profile ID
     * @param boardId ID of the board to remove
     * @param tutorEmail email extracted from JWT in the controller
     * @return updated child profile with boards
     * @throws ResourceNotFoundException if profile or board don't exist
     */
    public ChildProfileResponseDTO removeBoard(Long profileId, Long boardId, String tutorEmail) {
        log.info("Removing board ID: {} from child profile ID: {}", boardId, profileId);
        Tutor tutor = loadTutorByEmail(tutorEmail);
        ChildProfile profile = loadProfileForTutor(profileId, tutor.getId());

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with ID: " + boardId));

        profile.removeBoard(board); // Defined in ChildProfile entity

        ChildProfile updatedProfile = childProfileRepository.save(profile);
        log.info("Board ID: {} removed successfully from child profile ID: {}", boardId, profileId);

        return childProfileMapper.toResponseWithBoards(updatedProfile);
    }

    // ============= HELPER METHODS =============

    /**
     * Loads a tutor by email extracted from the JWT
     * @param email tutor's email
     * @return Tutor entity
     * @throws ResourceNotFoundException if no tutor exists with that email
     */
    private Tutor loadTutorByEmail(String email) {
        return tutorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found with email: " + email));
    }

    /**
     * Loads a child profile and verifies it belongs to the given tutor
     * Prevents tutors from accessing other tutors' children
     * @param profileId child profile ID
     * @param tutorId   authenticated tutor's ID
     * @return ChildProfile entity
     * @throws ResourceNotFoundException if not found or not owned by this tutor
     */
    private ChildProfile loadProfileForTutor(Long profileId, Long tutorId) {
        return childProfileRepository.findByIdAndTutorId(profileId, tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Child profile not found with ID: " + profileId));
    }
}
