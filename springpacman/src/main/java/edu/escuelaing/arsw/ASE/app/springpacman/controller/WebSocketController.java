package edu.escuelaing.arsw.ASE.app.springpacman.controller;

import edu.escuelaing.arsw.ASE.app.springpacman.model.Player;
import edu.escuelaing.arsw.ASE.app.springpacman.model.GameMatrix;
import edu.escuelaing.arsw.ASE.app.springpacman.service.GameService;
import edu.escuelaing.arsw.ASE.app.springpacman.service.PlayerService;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * WebSocket controller for handling WebSocket connections and game interactions.
 * This class manages player connections, game state, and interactions via WebSocket.
 */
@Component
public class WebSocketController extends TextWebSocketHandler {

    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<String, Boolean> playerReadyStatus = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> playerNames = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, int[]> initialPositions = new ConcurrentHashMap<>();
    private static final int totalPlayers = 2;  
    private static final Gson gson = new Gson();
    private static final GameMatrix gameState = new GameMatrix(); 
    private boolean gameOver = false;
    private String winner = "";

    @Autowired
    private GameService gameService;

    private Timer timer;
    private int timeLeft = 180; 

    @Autowired
    private PlayerService playerService;

    /**
     * Called after a new WebSocket connection is established.
     * Adds the session to the list of active sessions and initializes player status.
     *
     * @param session the new WebSocket session
     * @throws Exception if an error occurs
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established: " + session.getId());
        synchronized (sessions) {
            sessions.add(session);
        }
        synchronized (playerReadyStatus) {
            playerReadyStatus.put(session.getId(), false); 
        }
        synchronized (playerNames) {
            playerNames.put(session.getId(), "Unknown"); 
        }

        updatePlayersStatus();
    }

    /**
     * Handles incoming WebSocket messages.
     * Processes messages related to player actions such as joining, moving, and capturing thieves.
     *
     * @param session the WebSocket session
     * @param message the incoming WebSocket message
     * @throws Exception if an error occurs
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (!session.isOpen() || gameOver) {
            return;
        }

        String payload = message.getPayload();
        Map<String, Object> data = gson.fromJson(payload, Map.class);

        if ("JOIN".equals(data.get("type"))) {
            handleJoinMessage(data, session);
        } else if ("PLAYER_READY".equals(data.get("type"))) {
            System.out.println("Setting player as ready for session: " + session.getId());
            synchronized (playerReadyStatus) {
                playerReadyStatus.put(session.getId(), true);
            }
            updatePlayersStatus();

            if (allPlayersReady()) {
                startGame();
            }
        } else if ("PLAYER_MOVE".equals(data.get("type"))) {
            handlePlayerMoveMessage(data, session);
        } else if ("CAPTURE_THIEF".equals(data.get("type"))) {
            handleCaptureThiefMessage(data, session);
        } else {
            sendGameStateToAllSessions();
        }
    }

    /**
     * Called after a WebSocket connection is closed.
     * Removes the session from the list of active sessions and updates player status.
     *
     * @param session the WebSocket session
     * @param status the close status
     * @throws Exception if an error occurs
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed: " + session.getId() + ", Status: " + status);
        synchronized (sessions) {
            sessions.remove(session);
        }
        synchronized (playerReadyStatus) {
            playerReadyStatus.remove(session.getId());
        }
        synchronized (playerNames) {
            playerNames.remove(session.getId());
        }
        synchronized (players) {
            players.remove(session.getId());
        }

        updatePlayersStatus();
    }

    /**
     * Updates the status of all players and sends the updated status to all sessions.
     *
     * @throws Exception if an error occurs
     */
    private void updatePlayersStatus() throws Exception {
        List<Map<String, Object>> playersStatus = new ArrayList<>();
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    Player player;
                    synchronized (players) {
                        player = players.get(session.getId());
                    }
                    if (player != null) {
                        Map<String, Object> playerStatus = new HashMap<>();
                        playerStatus.put("id", player.getId());
                        playerStatus.put("name", player.getName());
                        playerStatus.put("top", player.getTop());
                        playerStatus.put("left", player.getLeft());
                        playerStatus.put("isThief", player.isThief());
                        playerStatus.put("direction", player.getDirection());
                        playerStatus.put("paso1", player.getPaso1()); 
                        playerStatus.put("score", player.getScore()); 
                        playerStatus.put("lives", player.getLives()); 
                        synchronized (playerReadyStatus) {
                            playerStatus.put("isReady", playerReadyStatus.get(session.getId()));
                        }
                        playersStatus.add(playerStatus);
                    }
                }
            }
        }

        Map<String, Object> message = new HashMap<>();
        message.put("type", "UPDATE_PLAYERS");
        message.put("players", playersStatus);

        String jsonMessage = gson.toJson(message);
        sendToAllSessions(jsonMessage);
    }

    /**
     * Checks if all players are ready to start the game.
     *
     * @return true if all players are ready, false otherwise
     */
    private boolean allPlayersReady() {
        synchronized (playerReadyStatus) {
            boolean allReady = playerReadyStatus.size() >= totalPlayers &&
                    playerReadyStatus.values().stream().allMatch(Boolean::booleanValue);
            System.out.println("All players ready: " + allReady);
            return allReady;
        }
    }

    /**
     * Starts the game by initializing players and game state, and sends the start game message to all sessions.
     *
     * @throws Exception if an error occurs
     */
    private void startGame() throws Exception {
        List<Player> currentPlayers;
        synchronized (players) {
            currentPlayers = new ArrayList<>(players.values());
        }
        gameService.initializePlayers(currentPlayers);
        gameState.placePlayers(currentPlayers); 

        for (Player player : currentPlayers) {
            initialPositions.put(player.getId(), new int[]{player.getTop(), player.getLeft()});
        }

        startTimer();

        String initialMatrixJson = convertMatrixToJson(gameState);

        Map<String, Object> message = new HashMap<>();
        message.put("type", "START_GAME");
        message.put("matrix", gameState.getMatrix());
        message.put("players", currentPlayers);
        String jsonMessage = gson.toJson(message);
        sendToAllSessions(jsonMessage);
    }

    /**
     * Starts the game timer and schedules timer updates.
     */
    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (gameOver) {
                    timer.cancel();
                } else {
                    updateTime();
                }
            }
        }, 1000, 1000);
    }

    /**
     * Updates the game time and checks for game over conditions.
     */
    private void updateTime() {
        synchronized (sessions) {
            if (gameOver) {
                return;
            }

            boolean allDiamondsCaptured = true;
            for (int[] row : gameState.getMatrix()) {
                for (int cell : row) {
                    if (cell == 9) {
                        allDiamondsCaptured = false;
                        break;
                    }
                }
                if (!allDiamondsCaptured) {
                    break;
                }
            }

            if (allDiamondsCaptured) {
                gameOver = true;
                winner = "Thieves";
                sendGameOverMessage();
                return;
            }

            int totalThiefLives = 0;
            int totalPoliceScore = 0;
            int totalThiefScore = 0;

            for (Player player : players.values()) {
                if (player.isThief()) {
                    totalThiefLives += player.getLives();
                    totalThiefScore += player.getScore();
                } else {
                    totalPoliceScore += player.getScore();
                }
            }

            if (totalThiefLives == 0) {
                gameOver = true;
                winner = "Police";
                sendGameOverMessage();
                return;
            }

            if (--timeLeft <= 0) {
                gameOver = true;
                if (totalPoliceScore > totalThiefScore) {
                    winner = "Police";
                } else if (totalThiefScore > totalPoliceScore) {
                    winner = "Thieves";
                } else {
                    winner = "Draw";
                }
                sendGameOverMessage();
            } else {
                sendTimerUpdate();
            }
        }
    }

    /**
     * Sends a timer update message to all sessions.
     */
    private void sendTimerUpdate() {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "TIMER_UPDATE");
        message.put("timeLeft", timeLeft);
        String jsonMessage = gson.toJson(message);
        sendToAllSessions(jsonMessage);
    }

    /**
     * Sends a game over message to all sessions.
     */
    private void sendGameOverMessage() {
        timer.cancel();
        updatePlayerScores();
        Map<String, Object> message = new HashMap<>();
        message.put("type", "GAME_OVER");
        message.put("winner", winner);
        String jsonMessage = gson.toJson(message);
        sendToAllSessions(jsonMessage);
    }

    /**
     * Updates the scores of all players.
     */
    private void updatePlayerScores() {
        for (Player player : players.values()) {
            playerService.updateScore(player.getMongoId(), player.getScore());
        }
    }


    /**
     * Handles a join message from a player and updates the player list.
     *
     * @param data the message data
     * @param session the WebSocket session
     */
    private void handleJoinMessage(Map<String, Object> data, WebSocketSession session) {
        int playerId = ((Double) data.get("id")).intValue();
        String playerName = (String) data.get("name");
        int top = ((Double) data.get("top")).intValue();
        int left = ((Double) data.get("left")).intValue();
        boolean isThief = (Boolean) data.get("isThief");
        System.out.println(isThief);
        Player player = new Player(playerId, playerName, top, left, isThief);
        playerService.save(player);


        synchronized (players) {
            players.values().removeIf(p -> p.getId() == playerId);
            players.put(session.getId(), player);
        }
        synchronized (playerNames) {
            playerNames.put(session.getId(), playerName);
        }

        System.out.println("Player joined: " + player);

        try {
            sendPlayerListUpdate();
        } catch (Exception e) {
            System.out.println("Error in handleJoinMessage");
            e.printStackTrace();
        }
    }


    /**
     * Handles a player move message and updates the game state.
     *
     * @param data the message data
     * @param session the WebSocket session
     * @throws Exception if an error occurs
     */    
    private void handlePlayerMoveMessage(Map<String, Object> data, WebSocketSession session) throws Exception {
        int playerId = ((Double) data.get("id")).intValue();
        int previousTop = ((Double) data.get("previousTop")).intValue();
        int previousLeft = ((Double) data.get("previousLeft")).intValue();
        int top = ((Double) data.get("top")).intValue();
        int left = ((Double) data.get("left")).intValue();
        String direction = (String) data.get("direction");
        boolean paso1 = (Boolean) data.get("paso1"); 

        System.out.println("Recibiendo movimiento del jugador:" + ", "  + playerId + ", "  + top + ", "  + left + ", " + direction);
    
        synchronized (players) {
            Player player = players.get(session.getId());
            System.out.println(session.getId());
            System.out.println(player);

            if (player != null) {
                player.setTop(top);
                player.setLeft(left);
                player.setDirection(direction);
                player.setPaso1(paso1); 

                if (player.isThief() && player.getLives() == 0) {
                    return; 
                }

                if (gameState.getMatrix()[top][left] == 9  && player.isThief() ) { 
                    player.setScore(player.getScore() + 100);
                    gameState.setPosition(top, left, 0); 
                }

                gameService.updatePlayerPosition(player);
            }
        }
    
        synchronized (gameState) {
            
            gameState.setPosition(previousTop, previousLeft, 0); 
            gameState.setPosition(top, left, playerId); 
        }
    
        sendGameStateToAllSessions();
    }
    
    /**
     * Handles a capture thief message and updates the game state.
     *
     * @param data the message data
     * @param session the WebSocket session
     * @throws Exception if an error occurs
     */    
    private void handleCaptureThiefMessage(Map<String, Object> data, WebSocketSession session) throws Exception {
        int policeId = ((Double) data.get("policeId")).intValue();
        int thiefId = ((Double) data.get("thiefId")).intValue();
    
        System.out.println("Captura del ladrón " + thiefId + " por el policía " + policeId);
    
        synchronized (players) {
            Player police = null;
            Player thief = null;
    
            for (Player p : players.values()) {
                if (p.getId() == thiefId) {
                    thief = p;
                }
                if (p.getId() == policeId) {
                    police = p;
                }
                if (thief != null && police != null) {
                    break;
                }
            }
    
            if (thief != null) {
                thief.setLives(thief.getLives() - 1);
                int[] initialPosition = initialPositions.get(thiefId);
                if (initialPosition != null) {
                    int previousTop = thief.getTop();
                    int previousLeft = thief.getLeft();
    
                    thief.setTop(initialPosition[0]);
                    thief.setLeft(initialPosition[1]);
    
                    synchronized (gameState) {
                        gameState.setPosition(previousTop, previousLeft, 0); 
                        gameState.setPosition(initialPosition[0], initialPosition[1], thiefId);
                    }
    
                    gameService.updatePlayerPosition(thief);
                    sendInitialPositions(thief);
                }
    

                if (police != null) {
                    police.setScore(police.getScore() + 500);
                    gameService.updatePlayerPosition(police);
                }
            }
        }
    
        sendGameStateToAllSessions();
    }


    /**
     * Sends the initial positions of a thief to all sessions.
     *
     * @param thief the thief player
     */
    private void sendInitialPositions(Player thief) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "INITIAL_POSITION");
        message.put("id", thief.getId());
        message.put("top", thief.getTop());
        message.put("left", thief.getLeft());
        String jsonMessage = gson.toJson(message);

        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                try {
                    session.sendMessage(new TextMessage(jsonMessage));
                } catch (IOException e) {
                    System.err.println("Error sending initial position to session: " + session.getId());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Converts the game matrix to JSON format.
     *
     * @param matrix the game matrix
     * @return the JSON representation of the game matrix
     */    
    private String convertMatrixToJson(GameMatrix matrix) {
        return gson.toJson(matrix);
    }

    /**
     * Sends a message to all open sessions.
     *
     * @param message the message to send
     */    
    private synchronized void sendToAllSessions(String message) {
        List<WebSocketSession> closedSessions = new ArrayList<>();
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException | IllegalStateException e) {
                        closedSessions.add(session);
                        System.err.println("Error sending message to session: " + session.getId());
                        e.printStackTrace();
                    }
                } else {
                    closedSessions.add(session);
                }
            }
            sessions.removeAll(closedSessions);
        }
    }

    /**
     * Sends an updated player list to all sessions.
     *
     * @throws Exception if an error occurs
     */    
    private void sendPlayerListUpdate() throws Exception {
        List<Map<String, Object>> playersStatus = new ArrayList<>();
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    Player player;
                    synchronized (players) {
                        player = players.get(session.getId());

                    }
                    if (player != null) {
                        Map<String, Object> playerStatus = new HashMap<>();
                        playerStatus.put("id", player.getId());
                        playerStatus.put("name", player.getName());
                        playerStatus.put("top", player.getTop());
                        playerStatus.put("left", player.getLeft());
                        playerStatus.put("isThief", player.isThief());
                        playerStatus.put("direction", player.getDirection());


                        synchronized (playerReadyStatus) {
                            playerStatus.put("isReady", playerReadyStatus.get(session.getId()));
                        }
                        playersStatus.add(playerStatus);
                    }
                }
            }
        }

        System.out.println("Current players: " + playersStatus);

        Map<String, Object> message = new HashMap<>();
        message.put("type", "UPDATE_PLAYERS");
        message.put("players", playersStatus);

        String jsonMessage = gson.toJson(message);

        sendToAllSessions(jsonMessage);
    }

    /**
     * Sends the updated game state to all sessions.
     *
     * @throws Exception if an error occurs
     */
    private void sendGameStateToAllSessions() throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "UPDATE_GAME_STATE");
        message.put("matrix", gameState.getMatrix());
        message.put("players", new ArrayList<>(players.values())); 
        String jsonMessage = gson.toJson(message);
    
        sendToAllSessions(jsonMessage);
    }
}

