package edu.escuelaing.arsw.ASE.app.springpacman.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling login-related requests.
 * This controller manages player data assignment for thieves and police officers.
 */
@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {

    private int currentThiefIndex = 0;
    private int currentPoliceIndex = 0;

    private final int[][] thiefPositions = {
        {2, 2}, {3, 2}, {4, 2}, {8, 2}
    };

    private final int[][] policePositions = {
        {22, 37}, {23, 37}, {24, 37}, {25, 37}
    };


    /**
     * Resets the indices for thief and police positions.
     * This method is useful for reinitializing the state of the controller.
     */
    public void resetIndices() {
        this.currentThiefIndex = 0;
        this.currentPoliceIndex = 0;
    }

    /**
     * Retrieves player data based on the role (thief or police).
     * Assigns a unique position to the player and increments the index for the next assignment.
     *
     * @param isThief boolean indicating if the player is a thief (true) or a police officer (false)
     * @return ResponseEntity containing player data if successful, or a bad request status if no positions are available
     */
    @GetMapping("/getPlayerData")
    public synchronized ResponseEntity<Map<String, Object>> getPlayerData(@RequestParam boolean isThief) {
        Map<String, Object> playerData = new HashMap<>();

        if (isThief) {
            if (currentThiefIndex >= thiefPositions.length) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            playerData.put("id", currentThiefIndex + 1);
            playerData.put("top", thiefPositions[currentThiefIndex][0]);
            playerData.put("left", thiefPositions[currentThiefIndex][1]);
            currentThiefIndex++;
        } else {
            if (currentPoliceIndex >= policePositions.length) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            playerData.put("id", currentPoliceIndex + 5);
            playerData.put("top", policePositions[currentPoliceIndex][0]);
            playerData.put("left", policePositions[currentPoliceIndex][1]);
            currentPoliceIndex++;
        }

        return ResponseEntity.ok(playerData);
    }
}
