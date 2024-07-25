package edu.escuelaing.arsw.ASE.app.springpacman.model;

import java.util.List;

/**
 * Represents the game matrix for the game.
 * This class manages the grid-based layout of the game, including the positions of players, diamonds, obstacles, and bases.
 */
public class GameMatrix {
    private int[][] matrix;
    private int rows = 30;
    private int cols = 40; 

    /**
     * Constructs a new GameMatrix and initializes the matrix with static elements.
     * This includes initializing the matrix, placing static diamonds, obstacles, and bases.
     */
    public GameMatrix() {
        if (matrix == null) {
            this.matrix = new int[rows][cols];
            initializeMatrix();
            placeStaticDiamonds();
            placeStaticObstacles();
            placeStaticBases();
        } else {
            System.out.println("Ya existe una matrix creada");
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setPosition(int row, int col, int value) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            matrix[row][col] = value;
        }
    }


    /**
     * Initializes the matrix with default values.
     * Sets all positions in the matrix to 0.
     */
    private void initializeMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = 0; 
            }
        }
    }

    /**
     * Places static diamonds in predefined positions within the matrix.
     * Diamonds are represented by the value 9 in the matrix.
     */
    private void placeStaticDiamonds() {
        int[][] diamonds = {
            {8, 10}, {8, 18}, {6, 15}, {15, 20}, {1, 33},
            {1, 23}, {4, 25}, {15, 25}, {18, 25}, {23, 25},
            {28, 25}, {13, 15}, {13, 20}, {13, 8}, {13, 2},
            {23, 15}, {28, 15}, {18, 15}, {28, 5}, {1, 35},
            {1, 36}, {1, 38}, {2, 37}, {2, 35}, {3, 36},
            {13, 38}, {27, 2}, {26, 2}, {30, 1}, {25, 1},
            {14, 15}, {1, 15}, {30, 15}, {30, 16}, {30, 19},
            {15, 2}, {20, 3}, {10, 10}, {38, 5}, {35, 7},
            {13, 30}, {13, 32}, {14, 34}, {14, 36}, {10, 36},
            {9, 35}, {8, 36}, {26, 34}, {27, 34}, {5, 38},
        };

        for (int[] diamond : diamonds) {
            int x = diamond[1];
            int y = diamond[0];
            if (y >= 0 && y < rows && x >= 0 && x < cols) {
                matrix[y][x] = 9; 

            }
        }
    }

    /**
     * Places static obstacles in predefined positions within the matrix.
     * Obstacles are represented by the value 10 in the matrix.
     */    
    private void placeStaticObstacles() {
        int[][] obstacles = {
            {7, 7}, {17, 17}, {22, 27}, {5, 7}, {2, 7},
            {10, 7}, {5, 7}, {5, 5}, {15, 7}, {15, 10},
            {15, 12}, {15, 15}, {15, 17}, {10, 10}, {10, 12},
            {10, 15}, {10, 17}, {2, 22}, {5, 22}, {7, 22},
            {10, 22}, {12, 22}, {15, 22}, {17, 22}, {20, 22},
            {22, 22}, {25, 22}, {22, 17}, {25, 17}, {27, 17},
            {15, 5}, {17, 5}, {20, 10}, {22, 10}, {25, 10},
            {0, 27}, {2, 27}, {5, 27}, {7, 27}, {25, 27},
            {27, 27}, {7, 27}, {7, 27}, {15, 27}, {15, 30},
            {15, 32}, {15, 35}, {15, 37}, {15, 35}
        };

        for (int[] obstacle : obstacles) {
            int x = obstacle[1];
            int y = obstacle[0];
            if (y >= 0 && y < rows && x >= 0 && x < cols) {
                matrix[y][x] = 10;
            }
        }
    }

    /**
     * Places static bases in predefined positions within the matrix.
     * Thief bases are represented by the value 11, and police bases by the value 12 in the matrix.
     */    
    private void placeStaticBases() {
        int[][] basesThief = {
            {0, 0}
        };

        for (int[] base : basesThief) {
            int x = base[1];
            int y = base[0];
            if (y >= 0 && y < rows && x >= 0 && x < cols) {
                matrix[y][x] = 11; 

            }
        }

        int[][] basesPolice = {
            {20, 35}
        };

        for (int[] base : basesPolice) {
            int x = base[1];
            int y = base[0];
            if (y >= 0 && y < rows && x >= 0 && x < cols) {
                matrix[y][x] = 12;
            }
        }
    }
    
    /**
     * Places players in the matrix based on their positions.
     * Clears previous player positions before placing new ones.
     *
     * @param players the list of players to place in the matrix
     */
    public void placePlayers(List<Player> players) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] >= 1 && matrix[i][j] <= 8) {
                    matrix[i][j] = 0;
                }
            }
        }

        for (Player player : players) {
            int x = player.getLeft();
            int y = player.getTop();
            if (y >= 0 && y < rows && x >= 0 && x < cols) {
                int playerId = player.getId();
                if (playerId >= 1 && playerId <= 8) {
                    matrix[y][x] = playerId; 
;
                }
            }
        }
    }

    public void setMatrix(int[][] matrix2) {
        this.matrix = matrix2;
    }
}
