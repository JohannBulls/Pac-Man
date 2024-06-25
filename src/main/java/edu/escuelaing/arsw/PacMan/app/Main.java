package edu.escuelaing.arsw.PacMan.app;

public class Main {
    public static void main(String[] args) {
        int rows = 9;  // Número de filas del tablero
        int cols = 12;  // Número de columnas del tablero

        Board board = new Board(rows, cols);
        board.drawBoard();
    }
}
