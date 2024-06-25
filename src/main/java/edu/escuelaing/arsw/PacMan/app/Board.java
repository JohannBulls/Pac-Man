package edu.escuelaing.arsw.PacMan.app;

import java.util.Random;

public class Board {
    private int rows;
    private int cols;
    private CellType[][] cells;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new CellType[rows][cols];

        initializeBoard();
    }

    private void initializeBoard() {
        Random random = new Random();

        // Llenar el tablero con celdas
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (random.nextDouble() < 0.28) {
                    cells[i][j] = CellType.BLUE;  // Celda azul
                } else {
                    cells[i][j] = CellType.PURPLE;  // Celda morada
                }

                // Asegurar que los bordes sean celdas negras
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                    cells[i][j] = CellType.BLUE;  // Celda negra
                }
            }
        }

        // Configurar la zona de Pac-Man (centro del tablero)
        int midRow = rows / 2;
        int midCol = cols / 2;
        for (int i = midRow - 1; i <= midRow + 1; i++) {
            for (int j = midCol - 4; j <= midCol + 4; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols) {
                    cells[i][j] = CellType.BLUE;  // Celda azul
                }
            }
        }

        // Colocar las celdas negras en la parte interna de la zona de Pac-Man
        for (int j = midCol - 3; j <= midCol + 3; j++) {
            if (midRow >= 0 && midRow < rows && j >= 0 && j < cols) {
                cells[midRow][j] = CellType.BLACK;  // Celda negra
            }
        }
        for (int i = midRow - 1; i <= midRow + 1; i++) {
            if (i >= 0 && i < rows && (midCol - 4 >= 0 && midCol - 4 < cols)) {
                cells[i][midCol - 4] = CellType.BLACK;  // Celda negra
            }
            if (i >= 0 && i < rows && (midCol + 4 >= 0 && midCol + 4 < cols)) {
                cells[i][midCol + 4] = CellType.BLACK;  // Celda negra
            }
        }
    }

    public void drawBoard() {
        // Imprimir el tablero
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(cells[i][j].getColorCode() + cells[i][j].getSymbol() + "\u001B[0m ");  // Reset de color
            }
            System.out.println();
        }
    }
}
