package co.edu.escuelaing.arsw;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

    @GetMapping("/generateMatrix")
    public int[][] generateMatrix() {
        // Ejemplo simple de generación de matriz 10x10
        int rows = 10;
        int cols = 10;
        int[][] matrix = new int[rows][cols];

        // Llena la matriz con valores de ejemplo (0 = camino, 1 = pared)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (Math.random() < 0.2) ? 1 : 0; // 20% paredes
            }
        }
        
        return matrix;
    }
}
