package edu.escuelaing.arsw.ASE.app.springpacman.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Controller for handling board-related requests in the Pacman game.
 */
@RestController
@RequestMapping("/api/pacman")
public class BoardController {

    /**
     * Generates a random matrix with Pacman game layout.
     * 
     * @return A 2D array representing the game board matrix where:
     *         - 1 represents walls or obstacles.
     *         - 0 represents open spaces.
     *         The size of the matrix is randomly determined with constraints for rows and columns.
     */
    @GetMapping("/generateMatrix")
    public int[][] generateMatrix() {
        int minRows = 8;
        int maxRows = 25;
        int minCols = 9;
        int maxCols = 42;

        Random rand = new Random();
        int rows = rand.nextInt((maxRows - minRows) + 1) + minRows;
        int cols = rand.nextInt((maxCols - minCols) + 1) + minCols;

        // Ensure rows and columns are even numbers for proper layout
        if (rows % 2 != 0) {
            rows++;
        }
        if (cols % 2 != 0) {
            cols++;
        }

        double fillProbability = 0.2;
        return generateMatrixContent(rows, cols, fillProbability);
    }

    /**
     * Generates the content of the matrix based on the specified dimensions and fill probability.
     * 
     * @param rows The number of rows in the matrix.
     * @param cols The number of columns in the matrix.
     * @param fillProbability The probability of filling a cell with 1 (wall) instead of 0 (empty space).
     * @return A 2D array representing the game board matrix with the given constraints.
     */
    private int[][] generateMatrixContent(int rows, int cols, double fillProbability) {
        int[][] matrix = new int[rows][cols];
        Random rand = new Random();

        // Initialize borders as walls
        for (int i = 0; i < rows; i++) {
            matrix[i][0] = 1;
            matrix[i][cols - 1] = 1;
        }

        for (int j = 0; j < cols; j++) {
            matrix[0][j] = 1;
            matrix[rows - 1][j] = 1;
        }

        // Generate matrix content with specific layout
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if ((i + 2 == rows / 2 && (j - 2 == cols / 2 || j + 1 == cols / 2 || j - 1 == cols / 2 || j + 2 == cols / 2)) ||
                    (i + 1 == rows / 2 && (j - 2 == cols / 2 || j + 2 == cols / 2)) ||
                    (i == rows / 2 && (j - 2 == cols / 2 || j + 2 == cols / 2)) ||
                    (i - 1 == rows / 2 && (j - 2 == cols / 2 || j - 1 == cols / 2 || j == cols / 2 || j + 1 == cols / 2 || j + 2 == cols / 2))) {
                    matrix[i][j] = 1;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }

        // Fill remaining cells based on probability
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][j] = rand.nextDouble() < fillProbability ? 1 : 0;
                }
            }
        }

        // Adjust specific cells to ensure proper layout
        matrix[(rows / 2) - 1][cols - 1] = 0;
        matrix[(rows / 2) - 1][0] = 0;
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if ((i + 3 == rows / 2 && (j - 3 == cols / 2 || j - 2 == cols / 2 || j - 1 == cols / 2 || j == cols / 2 || j + 1 == cols / 2 || j + 2 == cols / 2 || j + 3 == cols / 2)) ||
                    i + 2 == rows / 2 && (j - 3 == cols / 2 || j == cols / 2 || j + 3 == cols / 2) ||
                    (i + 1 == rows / 2 && (j - 3 == cols / 2 || j - 1 == cols / 2 || j + 1 == cols / 2 || j == cols / 2 || j + 3 == cols / 2)) ||
                    (i == rows / 2 && (j - 3 == cols / 2 || j - 1 == cols / 2 || j + 1 == cols / 2 || j == cols / 2 || j + 3 == cols / 2)) ||
                    (i - 1 == rows / 2 && (j - 3 == cols / 2 || j + 3 == cols / 2)) ||
                    (i - 2 == rows / 2 && (j - 3 == cols / 2 || j - 2 == cols / 2 || j - 1 == cols / 2 || j == cols / 2 || j + 1 == cols / 2 || j + 2 == cols / 2 || j + 3 == cols / 2))) {
                    matrix[i][j] = 0;
                }
            }
        }

        return matrix;
    }
}