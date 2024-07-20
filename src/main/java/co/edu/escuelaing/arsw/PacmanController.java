package co.edu.escuelaing.arsw;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class PacmanController {

    @GetMapping("/generateMatrix")
    public int[][] generateMatrix() {
        int minRows = 8;
        int maxRows = 25;
        int minCols = 9;
        int maxCols = 42;

        Random rand = new Random();
        int rows = rand.nextInt((maxRows - minRows) + 1) + minRows;
        int cols = rand.nextInt((maxCols - minCols) + 1) + minCols;

        if (rows % 2 != 0) {
            rows++;
        }
        if (cols % 2 == 0) {
            cols++;
        }

        double fillProbability = 0.4;
        int[][] matriz = generateMatrix(rows, cols, fillProbability);
        return matriz;
    }

    public int[][] generateMatrix(int rows, int cols, double fillProbability) {
        int[][] matriz = new int[rows][cols];
        Random rand = new Random();

        for (int i = 0; i < rows; i++) {
            matriz[i][0] = 1;
            matriz[i][cols - 1] = 1;
        }

        for (int j = 0; j < cols; j++) {
            matriz[0][j] = 1;
            matriz[rows - 1][j] = 1;
        }

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if ((i + 2 == rows / 2 && (j - 2 == cols / 2 || j + 1 == cols / 2 || j - 1 == cols / 2 || j + 2 == cols / 2)) ||
                    (i + 1 == rows / 2 && (j - 2 == cols / 2 || j + 2 == cols / 2)) ||
                    (i == rows / 2 && (j - 2 == cols / 2 || j + 2 == cols / 2)) ||
                    (i - 1 == rows / 2 && (j - 2 == cols / 2 || j - 1 == cols / 2 || j == cols / 2 || j + 1 == cols / 2 || j + 2 == cols / 2))) {
                    matriz[i][j] = 1;
                } else {
                    matriz[i][j] = 0;
                }
            }
        }

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (matriz[i][j] == 0) {
                    matriz[i][j] = rand.nextDouble() < fillProbability ? 1 : 0;
                }
            }
        }
        matriz[(rows / 2) - 1][cols - 1] = 0;
        matriz[(rows / 2) - 1][0] = 0;
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if ((i + 3 == rows / 2 && (j - 3 == cols / 2 || j - 2 == cols / 2 || j - 1 == cols / 2 || j == cols / 2 || j + 1 == cols / 2 || j + 2 == cols / 2 || j + 3 == cols / 2)) ||
                    i + 2 == rows / 2 && (j - 3 == cols / 2 || j == cols / 2 || j + 3 == cols / 2) ||
                    (i + 1 == rows / 2 && (j - 3 == cols / 2 || j - 1 == cols / 2 || j + 1 == cols / 2 || j == cols / 2 || j + 3 == cols / 2)) ||
                    (i == rows / 2 && (j - 3 == cols / 2 || j - 1 == cols / 2 || j + 1 == cols / 2 || j == cols / 2 || j + 3 == cols / 2)) ||
                    (i - 1 == rows / 2 && (j - 3 == cols / 2 || j + 3 == cols / 2)) ||
                    (i - 2 == rows / 2 && (j - 3 == cols / 2 || j - 2 == cols / 2 || j - 1 == cols / 2 || j == cols / 2 || j + 1 == cols / 2 || j + 2 == cols / 2 || j + 3 == cols / 2))) {
                    matriz[i][j] = 0;
                }
            }
        }

        return matriz;
    }
}
