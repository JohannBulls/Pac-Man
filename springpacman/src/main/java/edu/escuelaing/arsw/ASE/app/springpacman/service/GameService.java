package edu.escuelaing.arsw.ASE.app.springpacman.service;

import edu.escuelaing.arsw.ASE.app.springpacman.model.GameMatrix;
import edu.escuelaing.arsw.ASE.app.springpacman.model.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing the game state and player positions.
 * This service provides methods to initialize the game, update player positions, and retrieve the game matrix.
 */
@Service
public class GameService {
    private GameMatrix gameMatrix = new GameMatrix();
    private List<Player> players = new ArrayList<>();

    /**
     * Retrieves the initial game matrix.
     *
     * @return the initial game matrix
     */
    public GameMatrix getInitialMatrix() {
        return gameMatrix;
    }

    /**
     * Initializes the game with the provided list of players.
     * This method places the players on the game matrix.
     *
     * @param initialPlayers the list of players to initialize the game with
     * @return the updated game matrix after placing the players
     */    
    public GameMatrix initializePlayers(List<Player> initialPlayers) {
        this.players = initialPlayers;
        gameMatrix.placePlayers(players);
        return gameMatrix;
    }

    /**
     * Updates the position of a player in the game matrix.
     * This method finds the player by their ID, updates their position and direction,
     * and then updates the game matrix with the new player positions.
     *
     * @param playerMove the player with the new position and direction
     * @return the updated game matrix after moving the player
     */    
    public GameMatrix updatePlayerPosition(Player playerMove) {
        Player player = players.stream().filter(p -> p.getId() == playerMove.getId()).findFirst().orElse(null);
        if (player != null) {
            player.setLeft(playerMove.getLeft());
            player.setTop(playerMove.getTop());
            player.setDirection(playerMove.getDirection());
            gameMatrix.placePlayers(players);
        }
        return gameMatrix;
    }
}
