package edu.escuelaing.arsw.ASE.app.springpacman;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for handling Pacman game actions and requests.
 */
@RestController
@RequestMapping("/api")
public class PacmanController {

    /**
     * Endpoint to start the Pacman game.
     * 
     * @return A {@link String} message indicating that the game has started.
     */
    @GetMapping("/start")
    public String startGame() {
        return "Game Started";
    }

    /**
     * Endpoint to move Pacman in a specified direction.
     * 
     * @param payload A {@link Map} containing the move direction. The map should have a key "direction" with a {@link String} value representing the direction.
     * @return A {@link ResponseEntity} containing a message about the move status. The response status can be:
     *         - {@link HttpStatus#OK} if the direction is valid and the move is successful.
     *         - {@link HttpStatus#BAD_REQUEST} if the direction is missing or invalid.
     *         - {@link HttpStatus#INTERNAL_SERVER_ERROR} if there is an error processing the move.
     */
    @PostMapping("/move")
    public ResponseEntity<String> movePacman(@RequestBody Map<String, String> payload) {
        String direction = payload.get("direction");
        if (direction == null) {
            return ResponseEntity.badRequest().body("Direction is missing");
        }
        try {
            switch (direction) {
                case "w":
                    return ResponseEntity.ok("Pacman moved UP");
                case "s":
                    return ResponseEntity.ok("Pacman moved DOWN");
                case "a":
                    return ResponseEntity.ok("Pacman moved LEFT");
                case "d":
                    return ResponseEntity.ok("Pacman moved RIGHT");
                default:
                    return ResponseEntity.badRequest().body("Invalid direction: " + direction);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error moving Pacman: " + e.getMessage());
        }
    }
}
