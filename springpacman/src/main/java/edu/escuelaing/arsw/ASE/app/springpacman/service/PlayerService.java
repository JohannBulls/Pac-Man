package edu.escuelaing.arsw.ASE.app.springpacman.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.escuelaing.arsw.ASE.app.springpacman.model.Player;
import edu.escuelaing.arsw.ASE.app.springpacman.repository.PlayerRepository;

/**
 * Service for managing players.
 * This service provides methods to find top players by score, save players, and
 * update player scores.
 */
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Finds the top players sorted by their scores in descending order.
     *
     * @return a list of top players by score
     */
    public List<Player> findTopByScore() {
        return playerRepository.findByScore();
    }

    /**
     * Saves a player to the repository.
     *
     * @param player the player to save
     */
    public void save(Player player) {
        playerRepository.save(player);
    }

    /**
     * Updates the score of a player in the repository.
     * If the player is found by their MongoDB ID, their score is updated and saved.
     *
     * @param mongoId the MongoDB ID of the player
     * @param score   the new score to set for the player
     */
    public void updateScore(int mongoId, int score) {
        Player player = playerRepository.findById(mongoId);
        System.out.println("el player es :" + player);
        if (player != null) {
            player.setScore(score);
            playerRepository.save(player);
        }
    }

}
