import React, { useState, useEffect, useCallback } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import Board from "./Board";
import "../css/App.css";
import musicFile from "../assets/sounds/music.mp3";

const App = () => {
  const [board, setBoard] = useState([]);
  const [music] = useState(new Audio(musicFile));
  const [pacmanPosition, setPacmanPosition] = useState({ x: 0, y: 0 });
  const [ghostPosition, setGhostPosition] = useState({ x: 1, y: 1 }); // Inicializa la posición del fantasma

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
        // Calcula la posición inicial de Pacman
        const initialX = Math.floor(data.length / 2) + 2;
        const initialY = Math.floor(data[0].length / 2) + 1;
        setPacmanPosition({ x: initialX, y: initialY });

        // Calculate initial Ghost position
        const ghostInitialX = Math.floor(data.length / 2) - 1;
        const ghostInitialY = Math.floor(data[0].length / 2) - 1;
        setGhostPosition({ x: ghostInitialX, y: ghostInitialY });
      } catch (error) {
        console.error("Error fetching board:", error);
      }
    };

    fetchBoard();
  }, []);

  const startGame = () => {
    music.play();
    console.log("Game started");
  };

  const checkCollision = (pacman, ghost) => {
    if (pacman.x === ghost.x && pacman.y === ghost.y) {
      alert("Pacman ha sido atrapado por el fantasma. ¡Game Over!");
      // Detén el juego aquí si es necesario
      return true;
    }
    return false;
  };

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

      // Verifica si la nueva posición de Pacman colisiona con el muro
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

          // Verifica la colisión con el fantasma
          if (checkCollision({ x: newX, y: newY }, ghostPosition)) {
            // Puedes detener el juego aquí si es necesario
            music.pause(); // Ejemplo de detener la música
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
                ghostPosition={ghostPosition} // Pasa la posición del fantasma al componente Board
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
