package edu.escuelaing.arsw.ASE.app.springpacman.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import edu.escuelaing.arsw.ASE.app.springpacman.model.Player;

/**
 * Repository interface for Player entities.
 * This interface provides methods for interacting with the Player collection in MongoDB.
 */
public interface PlayerRepository  extends MongoRepository<Player, Integer>{

    /**
     * Finds all players and returns only their names and scores, sorted by score in descending order.
     *
     * @return a list of players with their names and scores
     */    
    @Query(value = "{}", fields = "{'name': 1, 'score': 1}", sort = "{'score': -1}")
    List<Player> findByScore();
    
    /**
     * Finds a player by their MongoDB ID.
     *
     * @param mongodb the MongoDB ID of the player
     * @return the player with the specified ID, or null if no player is found
     */
    Player findById(int mongodb);

}
