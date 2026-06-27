package com.caa.backend.service;

import com.caa.backend.dto.ChildProfileRequestDTO;
import com.caa.backend.dto.ResponseDTOs.ChildProfileResponseDTO;
import com.caa.backend.exception.ResourceNotFoundException;
import com.caa.backend.mapper.ChildProfileMapper;
import com.caa.backend.model.Board;
import com.caa.backend.model.ChildProfile;
import com.caa.backend.model.Tutor;
import com.caa.backend.model.enums.Level;
import com.caa.backend.model.enums.Role;
import com.caa.backend.repository.BoardRepository;
import com.caa.backend.repository.ChildProfileRepository;
import com.caa.backend.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildProfileServiceTest {

    @Mock private ChildProfileRepository childProfileRepository;
    @Mock private TutorRepository tutorRepository;
    @Mock private BoardRepository boardRepository;
    @Mock private ChildProfileMapper childProfileMapper;
    @Mock private FileStorageService fileStorageService;

    @InjectMocks
    private ChildProfileService childProfileService;

    private static final String TUTOR_EMAIL = "ana@example.com";
    private static final Long TUTOR_ID = 1L;
    private static final Long PROFILE_ID = 1L;
    private static final Long BOARD_ID = 10L;

    private Tutor tutor;
    private ChildProfile profile;
    private Board board;
    private ChildProfileResponseDTO profileResponseDTO;

    @BeforeEach
    void setUp() {
        tutor = new Tutor();
        tutor.setId(TUTOR_ID);
        tutor.setName("Ana García");
        tutor.setEmail(TUTOR_EMAIL);
        tutor.setRole(Role.TUTOR);

        profile = new ChildProfile();
        profile.setId(PROFILE_ID);
        profile.setName("Lucas");
        profile.setLevel(Level.LEVEL_1);
        tutor.addChildProfile(profile); // establishes bidirectional relation

        board = new Board();
        board.setId(BOARD_ID);
        board.setName("General básico");
        board.setLevel(1);
        board.setRows(3);
        board.setColumns(4);
        board.setIsPredefined(true);

        profileResponseDTO = ChildProfileResponseDTO.builder()
                .id(PROFILE_ID)
                .name("Lucas")
                .tutorId(TUTOR_ID)
                .level(Level.LEVEL_1)
                .boardCount(0)
                .build();
    }

    /******* shared stubs **************************************************/
    private void givenTutorExists() {
        when(tutorRepository.findByEmail(TUTOR_EMAIL)).thenReturn(Optional.of(tutor));
    }

    private void givenProfileBelongsToTutor() {
        when(childProfileRepository.findByIdAndTutorId(PROFILE_ID, TUTOR_ID))
                .thenReturn(Optional.of(profile));
    }

    /******* getAllProfiles **************************************************/
    @Test
    void shouldReturnProfileList_whenGetAllProfilesForTutor() {
        List<ChildProfile> profiles = List.of(profile);
        List<ChildProfileResponseDTO> responseDTOs = List.of(profileResponseDTO);

        givenTutorExists();
        when(childProfileRepository.findByTutorId(TUTOR_ID)).thenReturn(profiles);
        when(childProfileMapper.toResponseList(profiles)).thenReturn(responseDTOs);

        List<ChildProfileResponseDTO> result = childProfileService.getAllProfiles(TUTOR_EMAIL);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Lucas");
        verify(childProfileRepository).findByTutorId(TUTOR_ID);
        verify(childProfileMapper).toResponseList(profiles);
    }

    /******* getProfileById **************************************************/
    @Test
    void shouldReturnProfileWithBoards_whenIdBelongsToTutor() {
        givenTutorExists();
        when(childProfileRepository.findByIdAndTutorIdWithBoards(PROFILE_ID, TUTOR_ID))
                .thenReturn(Optional.of(profile));
        when(childProfileMapper.toResponseWithBoards(profile)).thenReturn(profileResponseDTO);

        ChildProfileResponseDTO result = childProfileService.getProfileById(PROFILE_ID, TUTOR_EMAIL);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(PROFILE_ID);
        verify(childProfileRepository).findByIdAndTutorIdWithBoards(PROFILE_ID, TUTOR_ID);
        verify(childProfileMapper).toResponseWithBoards(profile);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenIdDoesNotBelongToTutor() {
        Long unknownProfileId = 99L;
        givenTutorExists();
        when(childProfileRepository.findByIdAndTutorIdWithBoards(unknownProfileId, TUTOR_ID))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> childProfileService.getProfileById(unknownProfileId, TUTOR_EMAIL));
        verify(childProfileMapper, never()).toResponseWithBoards(any());
    }

    /******* createProfile **************************************************/
    @Test
    void shouldSaveProfileAndAutoAssignGeneralBoard_whenCreateProfile() {
        ChildProfileRequestDTO dto = new ChildProfileRequestDTO("Lucas", null, null, Level.LEVEL_1);

        ChildProfile newProfile = new ChildProfile();
        newProfile.setName("Lucas");
        newProfile.setLevel(Level.LEVEL_1);

        ChildProfile savedProfile = new ChildProfile();
        savedProfile.setId(2L);
        savedProfile.setName("Lucas");

        ChildProfileResponseDTO savedResponseDTO = ChildProfileResponseDTO.builder()
                .id(2L).name("Lucas").tutorId(TUTOR_ID).level(Level.LEVEL_1).boardCount(1).build();

        givenTutorExists();
        when(childProfileMapper.toEntity(any(ChildProfileRequestDTO.class))).thenReturn(newProfile);
        when(boardRepository.findByIsPredefined(true)).thenReturn(List.of(board)); // board name contains "general"
        when(childProfileRepository.save(newProfile)).thenReturn(savedProfile);
        when(childProfileMapper.toResponse(savedProfile)).thenReturn(savedResponseDTO);

        ChildProfileResponseDTO result = childProfileService.createProfile(dto, TUTOR_EMAIL);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);

        assertThat(newProfile.getAssignedBoards()).containsExactly(board);
        verify(childProfileRepository).save(newProfile);
        verify(childProfileMapper).toResponse(savedProfile);
    }

    @Test
    void shouldSaveProfileWithoutAutoAssignment_whenNoGeneralBoardExists() {
        ChildProfileRequestDTO dto = new ChildProfileRequestDTO("Lucas", null, null, Level.LEVEL_1);

        ChildProfile newProfile = new ChildProfile();
        newProfile.setName("Lucas");
        newProfile.setLevel(Level.LEVEL_1);

        givenTutorExists();
        when(childProfileMapper.toEntity(any(ChildProfileRequestDTO.class))).thenReturn(newProfile);
        when(boardRepository.findByIsPredefined(true)).thenReturn(List.of());
        when(childProfileRepository.save(newProfile)).thenReturn(newProfile);
        when(childProfileMapper.toResponse(newProfile)).thenReturn(profileResponseDTO);

        childProfileService.createProfile(dto, TUTOR_EMAIL);

        assertThat(newProfile.getAssignedBoards()).isEmpty();
        verify(childProfileRepository).save(newProfile);
    }

    /******* updateProfile **************************************************/
    @Test
    void shouldUpdateAndReturnProfile_whenUpdateProfile() {
        ChildProfileRequestDTO updateRequest = new ChildProfileRequestDTO("Lucas Updated", null, null, Level.LEVEL_2);

        ChildProfile updatedEntity = new ChildProfile();
        updatedEntity.setId(PROFILE_ID);
        updatedEntity.setName("Lucas Updated");
        updatedEntity.setLevel(Level.LEVEL_2);

        ChildProfileResponseDTO updatedResponseDTO = ChildProfileResponseDTO.builder()
                .id(PROFILE_ID).name("Lucas Updated").tutorId(TUTOR_ID).level(Level.LEVEL_2).build();

        givenTutorExists();
        givenProfileBelongsToTutor();
        when(childProfileRepository.save(profile)).thenReturn(updatedEntity);
        when(childProfileMapper.toResponse(updatedEntity)).thenReturn(updatedResponseDTO);

        ChildProfileResponseDTO result = childProfileService.updateProfile(PROFILE_ID, updateRequest, TUTOR_EMAIL);

        assertThat(result.getName()).isEqualTo("Lucas Updated");
        assertThat(result.getLevel()).isEqualTo(Level.LEVEL_2);

        verify(childProfileMapper).updateEntityFromRequest(any(ChildProfileRequestDTO.class), eq(profile));
        verify(childProfileRepository).save(profile);
    }

    /******* deleteProfile **************************************************/
    @Test
    void shouldDeleteProfileAndClearBidirectionalRelation_whenDeleteProfile() {
        givenTutorExists();
        givenProfileBelongsToTutor();

        childProfileService.deleteProfile(PROFILE_ID, TUTOR_EMAIL);

        verify(childProfileRepository).delete(profile);

        assertThat(tutor.getChildProfiles()).doesNotContain(profile);
        assertThat(profile.getTutor()).isNull();
    }

    /******* assignBoard **************************************************/
    @Test
    void shouldAssignBoardWithoutDuplicates_whenAssignBoard() {
        ChildProfileResponseDTO responseWithBoard = ChildProfileResponseDTO.builder()
                .id(PROFILE_ID).name("Lucas").tutorId(TUTOR_ID).boardCount(1).build();

        givenTutorExists();

        when(childProfileRepository.findByIdAndTutorId(PROFILE_ID, TUTOR_ID))
                .thenReturn(Optional.of(profile));
        when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));
        when(childProfileRepository.save(profile)).thenReturn(profile);
        when(childProfileMapper.toResponseWithBoards(profile)).thenReturn(responseWithBoard);

        childProfileService.assignBoard(PROFILE_ID, BOARD_ID, TUTOR_EMAIL);
        childProfileService.assignBoard(PROFILE_ID, BOARD_ID, TUTOR_EMAIL);

        assertThat(profile.getAssignedBoards()).hasSize(1).containsExactly(board);
        verify(childProfileRepository, times(2)).save(profile);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenAssignNonExistentBoard() {
        givenTutorExists();
        when(childProfileRepository.findByIdAndTutorId(PROFILE_ID, TUTOR_ID))
                .thenReturn(Optional.of(profile));
        when(boardRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> childProfileService.assignBoard(PROFILE_ID, 99L, TUTOR_EMAIL));
        verify(childProfileRepository, never()).save(any());
    }

    /******* removeBoard **************************************************/
    @Test
    void shouldRemoveBoardAssignment_whenRemoveBoard() {
        profile.assignBoard(board);
        assertThat(profile.getAssignedBoards()).hasSize(1); // pre-condition

        givenTutorExists();
        when(childProfileRepository.findByIdAndTutorId(PROFILE_ID, TUTOR_ID))
                .thenReturn(Optional.of(profile));
        when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));
        when(childProfileRepository.save(profile)).thenReturn(profile);
        when(childProfileMapper.toResponseWithBoards(profile)).thenReturn(profileResponseDTO);

        childProfileService.removeBoard(PROFILE_ID, BOARD_ID, TUTOR_EMAIL);

        assertThat(profile.getAssignedBoards()).doesNotContain(board);
        verify(childProfileRepository).save(profile);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenRemoveNonExistentBoard() {
        givenTutorExists();
        when(childProfileRepository.findByIdAndTutorId(PROFILE_ID, TUTOR_ID))
                .thenReturn(Optional.of(profile));
        when(boardRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> childProfileService.removeBoard(PROFILE_ID, 99L, TUTOR_EMAIL));
        verify(childProfileRepository, never()).save(any());
    }
}
