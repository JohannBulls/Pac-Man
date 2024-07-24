import React, { useState, useEffect, useCallback } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import Board from "./Board";
import "../css/App.css";
import musicFile from "../assets/sounds/music.mp3";

/**
 * Main application component for the Pacman game.
 * <p>
 * This component initializes the game board, handles Pacman and ghost movements,
 * and manages game state including collision detection and music playback.
 * </p>
 * 
 * @component
 */
const App = () => {
  /**
   * State to store the game board matrix.
   * @type {Array<Array<number>>}
   */
  const [board, setBoard] = useState([]);

  /**
   * State to handle music playback.
   * @type {Audio}
   */
  const [music] = useState(new Audio(musicFile));

  /**
   * State to track Pacman's position on the board.
   * @type {{ x: number, y: number }}
   */
  const [pacmanPosition, setPacmanPosition] = useState({ x: 0, y: 0 });

  /**
   * State to track the ghost's position on the board.
   * @type {{ x: number, y: number }}
   */
  const [ghostPosition, setGhostPosition] = useState({ x: 1, y: 1 }); // Initializes ghost's position

  /**
   * Fetches the game board from the server and sets initial positions for Pacman and the ghost.
   * <p>
   * This effect runs once when the component mounts, fetching the board matrix and setting the initial positions.
   * </p>
   * 
   * @async
   */
  useEffect(() => {
    const fetchBoard = async () => {
      try {
        const response = await fetch(
          "http://localhost:8080/api/pacman/generateMatrix"
        );
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        setBoard(data);

        // Calculate initial positions for Pacman and the ghost
        const initialX = Math.floor(data.length / 2) + 2;
        const initialY = Math.floor(data[0].length / 2) + 1;
        setPacmanPosition({ x: initialX, y: initialY });

        const ghostInitialX = Math.floor(data.length / 2) - 1;
        const ghostInitialY = Math.floor(data[0].length / 2) - 1;
        setGhostPosition({ x: ghostInitialX, y: ghostInitialY });
      } catch (error) {
        console.error("Error fetching board:", error);
      }
    };

    fetchBoard();
  }, []);

  /**
   * Starts the game by playing the background music.
   * <p>
   * This function is triggered when the "Start Game" button is clicked.
   * </p>
   */
  const startGame = () => {
    music.play();
    console.log("Game started");
  };

  /**
   * Checks for collision between Pacman and the ghost.
   * <p>
   * Displays an alert and pauses the music if Pacman collides with the ghost.
   * </p>
   * 
   * @param {Object} pacman - The position of Pacman.
   * @param {Object} ghost - The position of the ghost.
   * @returns {boolean} - True if collision occurs, otherwise false.
   */
  const checkCollision = (pacman, ghost) => {
    if (pacman.x === ghost.x && pacman.y === ghost.y) {
      alert("Pacman ha sido atrapado por el fantasma. ¡Game Over!");
      music.pause(); // Example of stopping the music
      return true;
    }
    return false;
  };

  /**
   * Handles keyboard input to move Pacman.
   * <p>
   * Moves Pacman based on the key pressed, updates the position, and checks for collisions.
   * </p>
   * 
   * @param {KeyboardEvent} event - The keyboard event triggered by key presses.
   * @async
   */
  const handleKeyDown = useCallback(
    async (event) => {
      let newX = pacmanPosition.x;
      let newY = pacmanPosition.y;
      let direction;

      switch (event.key) {
        case "w":
          newX = Math.max(pacmanPosition.x - 1, 0);
          direction = "w";
          break;
        case "s":
          newX = Math.min(pacmanPosition.x + 1, board.length - 1);
          direction = "s";
          break;
        case "a":
          newY = Math.max(pacmanPosition.y - 1, 0);
          direction = "a";
          break;
        case "d":
          newY = Math.min(pacmanPosition.y + 1, board[0]?.length - 1);
          direction = "d";
          break;
        default:
          return;
      }

      // Check if Pacman's new position collides with a wall
      if (board[newX][newY] !== 1) {
        try {
          const response = await fetch("http://localhost:8080/api/move", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ direction }),
          });

          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }

          const result = await response.text();
          console.log("Move result:", result);

          setPacmanPosition({ x: newX, y: newY });

          // Check collision with ghost
          if (checkCollision({ x: newX, y: newY }, ghostPosition)) {
            // Stop the game if necessary
          }
        } catch (error) {
          console.error("Error moving Pacman:", error);
        }
      } else {
        console.log("Collision with wall at:", newX, newY);
      }
    },
    [pacmanPosition, board, ghostPosition]
  );

  useEffect(() => {
    window.addEventListener("keydown", handleKeyDown);
    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [handleKeyDown]);

  return (
    <div className="App vh-100 overflow-hidden">
      <div className="container-fluid h-100">
        <div className="row h-100">
          <div className="col-lg-9 p-0">
            <div className="h-100 d-flex justify-content-center align-items-center">
              <Board
                board={board}
                pacmanPosition={pacmanPosition}
                ghostPosition={ghostPosition} // Passes the ghost position to the Board component
              />
            </div>
          </div>
          <div className="col-lg-3 bg-light p-0">
            <div className="p-4">
              <header className="App-header text-center bg-dark text-light py-3">
                <div className="container">
                  <h1 className="display-5">PACMAN GAME</h1>
                  <button className="btn btn-primary" onClick={startGame}>
                    Start Game
                  </button>
                </div>
              </header>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default App;
