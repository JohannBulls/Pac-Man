import React, { useEffect, useRef, useState } from "react";
import "../css/Board.css";
import pacmanImageSrc from "../assets/images/right.gif";
import ghostImageSrc from "../assets/images/ghost.gif"; // Asegúrate de que la ruta sea correcta

const Board = ({ board = [], pacmanPosition, ghostPosition }) => {
  const canvasRef = useRef(null);
  const [pacmanImg, setPacmanImg] = useState(null);
  const [ghostImg, setGhostImg] = useState(null); // Estado para la imagen del fantasma

  useEffect(() => {
    const imgPacman = new Image();
    imgPacman.src = pacmanImageSrc;
    imgPacman.onload = () => setPacmanImg(imgPacman);

    const imgGhost = new Image();
    imgGhost.src = ghostImageSrc; // Carga la imagen del fantasma
    imgGhost.onload = () => setGhostImg(imgGhost);
  }, []);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");

    const drawBoard = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      board.forEach((row, rowIndex) => {
        row.forEach((cell, cellIndex) => {
          let cellColor = "#fff";
          if (cell === 1) {
            cellColor = "#00f"; 
          } else if (cell === 2) {
            cellColor = "#eebbbb"; 
          }
          ctx.fillStyle = cellColor;
          ctx.fillRect(cellIndex * 30, rowIndex * 30, 30, 30); 
        });
      });

      if (pacmanImg) {
        ctx.drawImage(
          pacmanImg,
          pacmanPosition.y * 30, 
          pacmanPosition.x * 30, 
          30, 
          30 
        );
      }

      if (ghostImg) { // Dibuja el fantasma en el tablero
        ctx.drawImage(
          ghostImg,
          ghostPosition.y * 30, 
          ghostPosition.x * 30, 
          30, 
          30 
        );
      }
    };

    drawBoard();

    return () => {};
  }, [board, pacmanPosition, pacmanImg, ghostPosition, ghostImg]);

  return (
    <canvas 
      ref={canvasRef} 
      width={(board[0]?.length || 0) * 30} 
      height={(board.length || 0) * 30} 
      className="board-canvas"
    ></canvas>
  );
};

export default Board;
