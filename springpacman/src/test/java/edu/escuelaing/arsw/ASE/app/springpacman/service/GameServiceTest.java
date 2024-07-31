package edu.escuelaing.arsw.ASE.app.springpacman.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.escuelaing.arsw.ASE.app.springpacman.model.GameMatrix;
import edu.escuelaing.arsw.ASE.app.springpacman.model.Player;

public class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    void testGetInitialMatrix() {
        GameMatrix initialMatrix = gameService.getInitialMatrix();
        assertEquals(30, initialMatrix.getMatrix().length); 
        assertEquals(40, initialMatrix.getMatrix()[0].length); 
    }

    @Test
    void testInitializePlayers() {
        List<Player> players = Arrays.asList(
                new Player(1, "Player1", 2, 2, true),
                new Player(2, "Player2", 3, 3, false)
        );

        GameMatrix matrix = gameService.initializePlayers(players);
        assertEquals(1, matrix.getMatrix()[2][2]); 
        assertEquals(2, matrix.getMatrix()[3][3]); 
    }

    @Test
    void testUpdatePlayerPosition() {
        List<Player> players = Arrays.asList(
                new Player(1, "Player1", 2, 2, true),
                new Player(2, "Player2", 3, 3, false)
        );

        gameService.initializePlayers(players);

        Player playerMove = new Player(1, "Player1", 4, 4, true);
        gameService.updatePlayerPosition(playerMove);

        GameMatrix updatedMatrix = gameService.getInitialMatrix();
        assertEquals(1, updatedMatrix.getMatrix()[4][4]); 
        assertEquals(0, updatedMatrix.getMatrix()[2][2]); 
    }
}