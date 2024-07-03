package edu.escuelaing.arsw.ASE.app.springpacman;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PacmanController {

    @GetMapping("/start")
    public String startGame() {
        return "Game Started";
    }
    @PostMapping("/move")
    public ResponseEntity<String> movePacman(@RequestBody MoveRequest moveRequest) {
        try {
            String direction = moveRequest.getDirection();
            if ("UP".equals(direction)) {
                return ResponseEntity.ok("Pacman moved UP");
            } else if ("DOWN".equals(direction)) {
                return ResponseEntity.ok("Pacman moved DOWN");
            } else if ("LEFT".equals(direction)) {
                return ResponseEntity.ok("Pacman moved LEFT");
            } else if ("RIGHT".equals(direction)) {
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
