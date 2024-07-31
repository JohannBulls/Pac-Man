package edu.escuelaing.arsw.ASE.app.springpacman.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.escuelaing.arsw.ASE.app.springpacman.model.Player;
import edu.escuelaing.arsw.ASE.app.springpacman.repository.PlayerRepository;

public class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindTopByScore() {
        List<Player> players = Arrays.asList(
                new Player(1, "Player1", 2, 2, true),
                new Player(2, "Player2", 3, 3, false)
        );

        when(playerRepository.findByScore()).thenReturn(players);

        List<Player> result = playerService.findTopByScore();
        assertEquals(2, result.size());
        verify(playerRepository, times(1)).findByScore();
    }

    @Test
    void testSavePlayer() {
        Player player = new Player(1, "Player1", 2, 2, true);
        playerService.save(player);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void testUpdateScore() {
        Player player = new Player(1, "Player1", 2, 2, true);
        when(playerRepository.findById(player.getMongoId())).thenReturn(player);

        playerService.updateScore(player.getMongoId(), 100);

        verify(playerRepository, times(1)).findById(player.getMongoId());
        verify(playerRepository, times(1)).save(player);
        assertEquals(100, player.getScore());
    }

    @Test
    void testUpdateScorePlayerNotFound() {
        when(playerRepository.findById(anyInt())).thenReturn(null);

        playerService.updateScore(12345678, 100);

        verify(playerRepository, times(1)).findById(12345678);
        verify(playerRepository, never()).save(any(Player.class));
    }
}