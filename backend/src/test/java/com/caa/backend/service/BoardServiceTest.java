package com.caa.backend.service;

import com.caa.backend.dto.BoardRequestsDTOS.CreateBoardRequestDTO;
import com.caa.backend.dto.BoardRequestsDTOS.ResizeBoardRequestDTO;
import com.caa.backend.dto.ResponseDTOs.BoardResponseDTO;
import com.caa.backend.exception.ResourceNotFoundException;
import com.caa.backend.mapper.BoardMapper;
import com.caa.backend.model.Board;
import com.caa.backend.model.BoardPictogram;
import com.caa.backend.model.Tutor;
import com.caa.backend.model.enums.Role;
import com.caa.backend.repository.BoardRepository;
import com.caa.backend.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock private BoardRepository boardRepository;
    @Mock private BoardMapper boardMapper;
    @Mock private TutorRepository tutorRepository;

    @InjectMocks
    private BoardService boardService;

    private static final String TUTOR_EMAIL = "ana@example.com";
    private static final Long BOARD_ID = 1L;

    private Tutor tutor;
    private Board board;
    private BoardResponseDTO boardResponseDTO;

    @BeforeEach
    void setUp() {
        tutor = new Tutor();
        tutor.setId(1L);
        tutor.setName("Ana García");
        tutor.setEmail(TUTOR_EMAIL);
        tutor.setRole(Role.TUTOR);

        // Board owned by tutor, 3 rows x 4 columns, no pictograms
        board = new Board();
        board.setId(BOARD_ID);
        board.setName("Mi tablero");
        board.setDescription("Descripción");
        board.setRows(3);
        board.setColumns(4);
        board.setLevel(1);
        board.setIsPredefined(false);
        board.setOwner(tutor);

        boardResponseDTO = BoardResponseDTO.builder()
                .id(BOARD_ID)
                .name("Mi tablero")
                .rows(3)
                .columns(4)
                .level(1)
                .isPredefined(false)
                .pictogramCount(0)
                .totalCells(12)
                .build();
    }

    /************ getBoardById *************************************************/
    @Test
    void shouldReturnBoardWithPictograms_whenBoardExists() {
        // Given
        when(boardRepository.findByIdWithPictograms(BOARD_ID)).thenReturn(Optional.of(board));
        when(boardMapper.toResponseWithPictograms(board)).thenReturn(boardResponseDTO);

        // When
        BoardResponseDTO result = boardService.getBoardById(BOARD_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(BOARD_ID);
        verify(boardRepository).findByIdWithPictograms(BOARD_ID);
        verify(boardMapper).toResponseWithPictograms(board);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenBoardDoesNotExist() {
        // Given
        when(boardRepository.findByIdWithPictograms(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> boardService.getBoardById(99L));
        verify(boardMapper, never()).toResponseWithPictograms(any());
    }

    /*********** createBoard ***************************************************/
    @Test
    void shouldCreateBoardWithOwnerAndNotPredefined_whenCreateBoard() {
        // Given
        CreateBoardRequestDTO request = new CreateBoardRequestDTO(
                "Mi tablero", "Descripción", 3, 4, 1, false, null);

        Board newBoard = new Board();
        newBoard.setName("Mi tablero");
        newBoard.setRows(3);
        newBoard.setColumns(4);

        Board savedBoard = new Board();
        savedBoard.setId(2L);
        savedBoard.setName("Mi tablero");
        savedBoard.setOwner(tutor);
        savedBoard.setIsPredefined(false);

        when(tutorRepository.findByEmail(TUTOR_EMAIL)).thenReturn(Optional.of(tutor));
        when(boardMapper.toEntity(request)).thenReturn(newBoard);
        when(boardRepository.save(newBoard)).thenReturn(savedBoard);
        when(boardMapper.toResponseWithPictograms(savedBoard)).thenReturn(boardResponseDTO);

        // When
        BoardResponseDTO result = boardService.createBoard(request, TUTOR_EMAIL);

        // Then — service must have set owner and isPredefined on the entity
        assertThat(result).isNotNull();
        assertThat(newBoard.getOwner()).isSameAs(tutor);
        assertThat(newBoard.getIsPredefined()).isFalse();
        verify(boardRepository).save(newBoard);
        verify(boardMapper).toResponseWithPictograms(savedBoard);
    }

    /************************** copyBoard ***********************************************/
    @Test
    void shouldCreateCopyWithCopySuffixAndSameOwner_whenCopyBoard() {
        // Given
        board.setName("Tablero original");

        Board savedCopy = new Board();
        savedCopy.setId(99L);
        savedCopy.setName("Tablero original (copia)");

        BoardResponseDTO copyResponse = BoardResponseDTO.builder()
                .id(99L).name("Tablero original (copia)").build();

        when(boardRepository.findByIdWithPictograms(BOARD_ID)).thenReturn(Optional.of(board));
        when(tutorRepository.findByEmail(TUTOR_EMAIL)).thenReturn(Optional.of(tutor));

        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
        when(boardRepository.save(captor.capture())).thenReturn(savedCopy);
        when(boardMapper.toResponseWithPictograms(savedCopy)).thenReturn(copyResponse);

        // When
        BoardResponseDTO result = boardService.copyBoard(BOARD_ID, TUTOR_EMAIL);

        // Then
        Board capturedCopy = captor.getValue();
        assertThat(capturedCopy.getName()).isEqualTo("Tablero original (copia)");
        assertThat(capturedCopy.getOwner()).isSameAs(tutor);
        assertThat(capturedCopy.getIsPredefined()).isFalse();
        assertThat(result.getName()).isEqualTo("Tablero original (copia)");
    }

    @Test
    void shouldCopyAllPictogramDataFromOriginal_whenCopyBoard() {
        // Given — original board has one pictogram
        BoardPictogram original = new BoardPictogram();
        original.setId(1L);
        original.setPictogramId(42);
        original.setPictogramUrl("https://api.arasaac.org/pictograms/42");
        original.setText("comer");
        original.setPositionX(0);
        original.setPositionY(1);
        original.setBackgroundColor("#FFFFFF");
        board.addPictogram(original);

        Board savedCopy = new Board();
        savedCopy.setId(99L);

        when(boardRepository.findByIdWithPictograms(BOARD_ID)).thenReturn(Optional.of(board));
        when(tutorRepository.findByEmail(TUTOR_EMAIL)).thenReturn(Optional.of(tutor));

        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
        when(boardRepository.save(captor.capture())).thenReturn(savedCopy);
        when(boardMapper.toResponseWithPictograms(savedCopy)).thenReturn(boardResponseDTO);

        // When
        boardService.copyBoard(BOARD_ID, TUTOR_EMAIL);

        // Then — copied pictogram carries all field values from the original
        Board capturedCopy = captor.getValue();
        assertThat(capturedCopy.getPictograms()).hasSize(1);
        BoardPictogram copied = capturedCopy.getPictograms().get(0);
        assertThat(copied.getPictogramId()).isEqualTo(42);
        assertThat(copied.getText()).isEqualTo("comer");
        assertThat(copied.getPositionX()).isEqualTo(0);
        assertThat(copied.getPositionY()).isEqualTo(1);
        assertThat(copied.getBackgroundColor()).isEqualTo("#FFFFFF");
        // Copied pictogram must be a new object, not the same instance
        assertThat(copied).isNotSameAs(original);
    }

    /******* resizeBoard / loadBoardForOwner ********************************/
    @Test
    void shouldResizeSuccessfully_whenAllPictogramsInsideNewDimensions() {
        // Given — pictogram at column 1, row 1; new size 3 rows x 4 columns → all inside
        BoardPictogram pictogram = new BoardPictogram();
        pictogram.setPositionX(1); // 1 < 4 columns ✓
        pictogram.setPositionY(1); // 1 < 3 rows ✓
        board.addPictogram(pictogram);

        ResizeBoardRequestDTO request = new ResizeBoardRequestDTO(3, 4);
        Board resized = new Board();
        resized.setId(BOARD_ID);
        resized.setRows(3);
        resized.setColumns(4);

        when(boardRepository.findByIdWithPictograms(BOARD_ID)).thenReturn(Optional.of(board));
        when(boardRepository.save(board)).thenReturn(resized);
        when(boardMapper.toResponse(resized)).thenReturn(boardResponseDTO);

        // When
        BoardResponseDTO result = boardService.resizeBoard(BOARD_ID, request, TUTOR_EMAIL);

        // Then
        assertThat(result).isNotNull();
        assertThat(board.getRows()).isEqualTo(3);
        assertThat(board.getColumns()).isEqualTo(4);
        verify(boardRepository).save(board);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenPictogramOutsideNewDimensions() {
        // Given — pictogram at column 3, but new width is only 2 columns → 3 >= 2 → out of bounds
        BoardPictogram pictogram = new BoardPictogram();
        pictogram.setPositionX(3); // 3 >= 2 new columns → invalid
        pictogram.setPositionY(0);
        pictogram.setText("comer");
        board.addPictogram(pictogram);

        ResizeBoardRequestDTO request = new ResizeBoardRequestDTO(3, 2); // rows=3, columns=2

        when(boardRepository.findByIdWithPictograms(BOARD_ID)).thenReturn(Optional.of(board));

        // When / Then
        assertThrows(IllegalArgumentException.class,
                () -> boardService.resizeBoard(BOARD_ID, request, TUTOR_EMAIL));
        verify(boardRepository, never()).save(any());
    }

    @Test
    void shouldReturnBoard_whenLoadBoardForOwnerWithCorrectOwner() {
        // Given — board.owner.email matches tutorEmail
        ResizeBoardRequestDTO request = new ResizeBoardRequestDTO(3, 4);

        when(boardRepository.findByIdWithPictograms(BOARD_ID)).thenReturn(Optional.of(board));
        when(boardRepository.save(board)).thenReturn(board);
        when(boardMapper.toResponse(board)).thenReturn(boardResponseDTO);

        // When — no AccessDeniedException means loadBoardForOwner returned successfully
        BoardResponseDTO result = boardService.resizeBoard(BOARD_ID, request, TUTOR_EMAIL);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldThrowAccessDeniedException_whenLoadBoardForOwnerWithWrongOwner() {
        // Given — board belongs to a different tutor
        Tutor otherTutor = new Tutor();
        otherTutor.setId(2L);
        otherTutor.setEmail("otro@example.com");
        board.setOwner(otherTutor);

        ResizeBoardRequestDTO request = new ResizeBoardRequestDTO(3, 4);

        when(boardRepository.findByIdWithPictograms(BOARD_ID)).thenReturn(Optional.of(board));

        // When / Then
        assertThrows(AccessDeniedException.class,
                () -> boardService.resizeBoard(BOARD_ID, request, TUTOR_EMAIL));
        verify(boardRepository, never()).save(any());
    }

    @Test
    void shouldThrowAccessDeniedException_whenBoardHasNoOwner() {
        // Given — predefined board with no owner (owner == null)
        board.setOwner(null);
        board.setIsPredefined(true);

        ResizeBoardRequestDTO request = new ResizeBoardRequestDTO(3, 4);

        when(boardRepository.findByIdWithPictograms(BOARD_ID)).thenReturn(Optional.of(board));

        // When / Then
        assertThrows(AccessDeniedException.class,
                () -> boardService.resizeBoard(BOARD_ID, request, TUTOR_EMAIL));
    }

    /****************** deleteBoard *********************************************/

    @Test
    void shouldCallDeleteById_whenDeletingExistingBoard() {
        // Given
        when(boardRepository.existsById(BOARD_ID)).thenReturn(true);

        // When
        boardService.deleteBoard(BOARD_ID);

        // Then
        verify(boardRepository).existsById(BOARD_ID);
        verify(boardRepository).deleteById(BOARD_ID);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenDeletingNonExistentBoard() {
        // Given
        when(boardRepository.existsById(99L)).thenReturn(false);

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> boardService.deleteBoard(99L));
        verify(boardRepository, never()).deleteById(any());
    }
}
