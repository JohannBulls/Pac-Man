package edu.escuelaing.arsw.ASE.app.springpacman.controller;

import edu.escuelaing.arsw.ASE.app.springpacman.model.Player;
import edu.escuelaing.arsw.ASE.app.springpacman.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/players")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/top")
    public List<Player> getTopPlayers() {
        return playerService.findTopByScore();
    }
}
