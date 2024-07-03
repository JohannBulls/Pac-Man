package edu.escuelaing.arsw.ASE.app.springpacman;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PacmanController {

    @GetMapping("/start")
    public String startGame() {
        return "Game Started";
    }

    @PostMapping("/move")
    public ResponseEntity<String> movePacman(@RequestBody Map<String, String> payload) {
        String direction = payload.get("direction");
        if (direction == null) {
            return ResponseEntity.badRequest().body("Direction is missing");
        }
        try {
            if ("w".equals(direction)) {
                return ResponseEntity.ok("Pacman moved UP");
            } else if ("s".equals(direction)) {
                return ResponseEntity.ok("Pacman moved DOWN");
            } else if ("a".equals(direction)) {
                return ResponseEntity.ok("Pacman moved LEFT");
            } else if ("d".equals(direction)) {
                return ResponseEntity.ok("Pacman moved RIGHT");
            } else {
                return ResponseEntity.badRequest().body("Invalid direction: " + direction);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error moving Pacman: " + e.getMessage());
        }
    }
}
