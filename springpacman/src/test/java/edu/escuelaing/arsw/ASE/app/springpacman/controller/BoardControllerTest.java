package edu.escuelaing.arsw.ASE.app.springpacman.controller;

import edu.escuelaing.arsw.ASE.app.springpacman.model.BoardState;
import edu.escuelaing.arsw.ASE.app.springpacman.repository.BoardStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BoardControllerTest {

    @Mock
    private BoardStateRepository boardStateRepository;

    @InjectMocks
    private BoardController boardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBoardWithExistingBoardState() {
        // Given
        int[][] expectedBoard = new int[25][42];
        // Inicializa expectedBoard con valores esperados (esto puede ser específico para tu caso)
        String jsonBoard = "{\"estado del tablero en formato JSON de 25x42\"}"; // Ajusta según el formato real
        BoardState boardState = new BoardState();
        boardState.setId("default");
        boardState.setState(jsonBoard);
        when(boardStateRepository.findById(anyString())).thenReturn(Optional.of(boardState));

        // When
        ResponseEntity<int[][]> response = boardController.getBoard();
        int[][] actualBoard = response.getBody();

        // Then
        assertArrayEquals(expectedBoard, actualBoard);
    }

    @Test
    public void testGetBoardWithNewBoardState() {
        // Given
        when(boardStateRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        ResponseEntity<int[][]> response = boardController.getBoard();
        int[][] actualBoard = response.getBody();

        // Then
        // Verifica que la primera fila esté llena de 1s
        int[] firstRow = actualBoard[0];
        for (int value : firstRow) {
            assertEquals(1, value, "La primera fila debe estar llena de 1s.");
        }

        // Verifica que la última fila esté llena de 1s
        int[] lastRow = actualBoard[actualBoard.length - 1];
        for (int value : lastRow) {
            assertEquals(1, value, "La última fila debe estar llena de 1s.");
        }
    }
}
