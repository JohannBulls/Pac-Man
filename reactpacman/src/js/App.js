import React, { useState, useEffect, useCallback } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import Board from "./Board";
import "../css/App.css";
import musicFile from "../assets/sounds/music.mp3";
import moveSoundFile from "../assets/sounds/frightened.mp3";


const App = () => {
  const initialBoard = [
    [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
    [1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1],
    [1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1],
    [1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1],
    [1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1],
    [1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1],
    [1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1],
    [1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1],
    [0, 0, 0, 0, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 0, 0, 0, 0],
    [1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1],
    [2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2],
    [1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1],
    [0, 0, 0, 0, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 0, 0, 0, 0],
    [0, 0, 0, 0, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 0, 0, 0, 0],
    [1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1],
    [1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1],
    [1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1],
    [1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1],
    [1, 1, 2, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 2, 1, 1],
    [1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1],
    [1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1],
    [1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1],
    [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
  ];

  const [music] = useState(new Audio(musicFile));
  const [moveSound] = useState(new Audio(moveSoundFile));
  const [pacmanPosition, setPacmanPosition] = useState({ x: 13, y: 10 });

  const startGame = () => {
    music.play();
    console.log("Game started");
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
          newX = Math.min(pacmanPosition.x + 1, initialBoard.length - 1);
          direction = "s";
          break;
        case "a":
          newY = Math.max(pacmanPosition.y - 1, 0);
          direction = "a";
          break;
        case "d":
          newY = Math.min(pacmanPosition.y + 1, initialBoard[0].length - 1);
          direction = "d";
          break;
        default:
          return;
      }

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

        const result = await response.text(); // Assuming response is plain text
        console.log("Move result:", result);

        setPacmanPosition({ x: newX, y: newY });
      } catch (error) {
        console.error("Error moving Pacman:", error);
      }
    },
    [pacmanPosition, initialBoard.length, initialBoard[0].length] // Agregamos initialBoard como dependencia
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
            <div className="h-100 d-flex justify-content-center align-items-center" >
              <Board board={initialBoard} pacmanPosition={pacmanPosition} />
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