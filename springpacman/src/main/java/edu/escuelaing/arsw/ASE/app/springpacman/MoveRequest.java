package edu.escuelaing.arsw.ASE.app.springpacman;

/**
 * Represents a request to move in a specific direction within the Pacman game.
 */
public class MoveRequest {
    private String direction;

    /**
     * Gets the direction for the move request.
     * 
     * @return A {@link String} representing the direction of the move. Possible values might include "UP", "DOWN", "LEFT", "RIGHT".
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the direction for the move request.
     * 
     * @param direction A {@link String} representing the direction of the move. Expected values are "UP", "DOWN", "LEFT", "RIGHT".
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }
}
