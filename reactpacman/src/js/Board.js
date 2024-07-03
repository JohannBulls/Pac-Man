import React, { useEffect, useRef } from "react";
import "../css/Board.css";

const Board = ({ board, pacmanPosition }) => {
  const canvasRef = useRef(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");

    // Dibuja el tablero y Pacman inicialmente
    drawBoard(ctx, board, pacmanPosition);

    // Limpia el canvas y redibuja cuando cambia la posición de Pacman
    const clearAndRedraw = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      drawBoard(ctx, board, pacmanPosition);
    };

    // Maneja el redibujado del canvas cuando cambia la posición de Pacman
    clearAndRedraw();

    return () => {
      // Limpia los event listeners y recursos cuando el componente se desmonta
    };
  }, [board, pacmanPosition]);

  const drawBoard = (ctx, board, pacmanPosition) => {
    // Dibuja el tablero
    board.forEach((row, rowIndex) => {
      row.forEach((cell, cellIndex) => {
        let cellColor = "#fff";
        if (cell === 1) {
          cellColor = "#00f"; // Color de fondo para las celdas con valor 1
        } else if (cell === 2) {
          cellColor = "#eebbbb"; // Color de fondo para las celdas con valor 2
        }
        ctx.fillStyle = cellColor;
        ctx.fillRect(cellIndex * 30, rowIndex * 30, 30, 30); // Dibuja cada celda
      });
    });

    // Dibuja Pacman
    ctx.fillStyle = "#ff0"; // Color amarillo para Pacman
    ctx.beginPath();
    ctx.arc(
      pacmanPosition.y * 30 + 15, // x
      pacmanPosition.x * 30 + 15, // y
      15, // Radio del círculo
      0, // Ángulo inicial
      Math.PI * 2 // Ángulo final (círculo completo)
    );
    ctx.fill();
  };

  return (
    <canvas ref={canvasRef} width={board[0].length * 30} height={board.length * 30} className="board-canvas"></canvas>
  );
};

export default Board;
