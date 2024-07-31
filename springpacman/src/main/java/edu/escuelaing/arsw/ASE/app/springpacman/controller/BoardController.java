package edu.escuelaing.arsw.ASE.app.springpacman.controller;

import edu.escuelaing.arsw.ASE.app.springpacman.model.BoardState;
import edu.escuelaing.arsw.ASE.app.springpacman.repository.BoardStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardStateRepository boardStateRepository;

    private int[][] boardMatrix;
    private final int rows = 25;
    private final int cols = 42;
    private final double fillProbability = 0.2;

    @GetMapping
    public ResponseEntity<int[][]> getBoard() {
        Optional<BoardState> boardStateOptional = boardStateRepository.findById("default");
        if (boardStateOptional.isPresent()) {
            int[][] board = jsonToMatrix(boardStateOptional.get().getState());
            System.out.println("Matriz desde el controlador: " + Arrays.deepToString(board));
            return ResponseEntity.ok(board);
        } else {
            generateBoard();
            BoardState boardState = new BoardState();
            boardState.setId("default");
            boardState.setState(matrixToJson(boardMatrix));
            boardStateRepository.save(boardState);
            System.out.println("Matriz generada: " + Arrays.deepToString(boardMatrix));
            return ResponseEntity.ok(boardMatrix);
        }
    }

    private void generateBoard() {
        boardMatrix = new int[rows][cols];
        Random rand = new Random();

        // Inicializar bordes como muros
        for (int i = 0; i < rows; i++) {
            boardMatrix[i][0] = 1;
            boardMatrix[i][cols - 1] = 1;
        }

        for (int j = 0; j < cols; j++) {
            boardMatrix[0][j] = 1;
            boardMatrix[rows - 1][j] = 1;
        }

        // Generar contenido de la matriz con un diseño específico
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if ((i + 2 == rows / 2 && (j - 2 == cols / 2 || j + 1 == cols / 2 || j - 1 == cols / 2 || j + 2 == cols / 2)) ||
                    (i + 1 == rows / 2 && (j - 2 == cols / 2 || j + 2 == cols / 2)) ||
                    (i == rows / 2 && (j - 2 == cols / 2 || j + 2 == cols / 2)) ||
                    (i - 1 == rows / 2 && (j - 2 == cols / 2 || j - 1 == cols / 2 || j == cols / 2 || j + 1 == cols / 2 || j + 2 == cols / 2))) {
                    boardMatrix[i][j] = 1;
                } else {
                    boardMatrix[i][j] = 0;
                }
            }
        }

        // Rellenar celdas restantes basadas en probabilidad
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (boardMatrix[i][j] == 0) {
                    boardMatrix[i][j] = rand.nextDouble() < fillProbability ? 1 : 0;
                }
            }
        }

        // Ajustar celdas específicas para asegurar un diseño adecuado
        boardMatrix[(rows / 2) - 1][cols - 1] = 0;
        boardMatrix[(rows / 2) - 1][0] = 0;
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if ((i + 3 == rows / 2 && (j - 3 == cols / 2 || j - 2 == cols / 2 || j - 1 == cols / 2 || j == cols / 2 || j + 1 == cols / 2 || j + 2 == cols / 2 || j + 3 == cols / 2)) ||
                    (i + 2 == rows / 2 && (j - 3 == cols / 2 || j == cols / 2 || j + 3 == cols / 2)) ||
                    (i + 1 == rows / 2 && (j - 3 == cols / 2 || j - 1 == cols / 2 || j + 1 == cols / 2 || j == cols / 2 || j + 3 == cols / 2)) ||
                    (i == rows / 2 && (j - 3 == cols / 2 || j - 1 == cols / 2 || j + 1 == cols / 2 || j == cols / 2 || j + 3 == cols / 2)) ||
                    (i - 1 == rows / 2 && (j - 3 == cols / 2 || j + 3 == cols / 2)) ||
                    (i - 2 == rows / 2 && (j - 3 == cols / 2 || j - 2 == cols / 2 || j - 1 == cols / 2 || j == cols / 2 || j + 1 == cols / 2 || j + 2 == cols / 2 || j + 3 == cols / 2))) {
                    boardMatrix[i][j] = 0;
                }
            }
        }
    }

    private String matrixToJson(int[][] matrix) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(matrix);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }
    
    private int[][] jsonToMatrix(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, int[][].class);
        } catch (Exception e) {
            e.printStackTrace();
            return new int[rows][cols];
        }
    }
}
