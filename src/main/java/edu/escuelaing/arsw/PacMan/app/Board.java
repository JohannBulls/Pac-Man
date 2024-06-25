package edu.escuelaing.arsw.PacMan.app;

import java.util.Random;

public class Board {
    private int rows;
    private int cols;
    private Cell[][] cells;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        initializeBoard();
    }

    private void initializeBoard() {
        Random random = new Random();

        // Llenar el tablero con celdas
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (random.nextDouble() < 0.28) {
                    cells[i][j] = new Cell("0");  // Celda azul
                } else {
                    cells[i][j] = new Cell("*");  // Celda morada
                }

                // Asegurar que los bordes sean celdas negras
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                    cells[i][j] = new Cell("0");  // Celda negra
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Zona de Pac-Man (centro del tablero)
                int midRow = rows / 2;
                int midCol = cols / 2;
                if (midRow >i-2 && midRow<i+2 && midCol>j-4 && midCol<j+5) {
                    cells[i][j] = new Cell("0");  // Celda azul
                }
                if ((i == midRow && midCol< j+4 && j-3 < midCol) ||
                        ((j == midCol || j+1 == midCol) && i+1 == midRow) ||
                        (i == midRow && (j == 0 || j == cols - 1))) {
                    cells[i][j] = new Cell("-");  // Celda negra
                }
            }
        }
    }

    public void drawBoard() {
        // Imprimir el tablero
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(cells[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
    }
}
