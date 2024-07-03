import React, { useEffect, useRef, useState } from "react";
import "../css/Board.css";
import pacmanImageSrc from "../assets/images/right.gif";

const Board = ({ board, pacmanPosition }) => {
  const canvasRef = useRef(null);
  const [pacmanImg, setPacmanImg] = useState(null);

  useEffect(() => {
    const img = new Image();
    img.src = pacmanImageSrc;
    img.onload = () => setPacmanImg(img);
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
    };

    drawBoard();

    return () => {
    };
  }, [board, pacmanPosition, pacmanImg]);

  return (
    <canvas ref={canvasRef} width={board[0].length * 30} height={board.length * 30} className="board-canvas"></canvas>
  );
};

export default Board;
