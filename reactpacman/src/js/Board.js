import React, { useEffect, useRef, useState } from "react";
import "../css/Board.css";
import pacmanImageSrc from "../assets/images/right.gif";
import ghostImageSrc from "../assets/images/ghost.gif"; 

/**
 * Renders the game board on a canvas, displaying Pacman and the ghost.
 * <p>
 * This component uses a canvas element to draw the game board and sprites for Pacman and the ghost.
 * </p>
 * 
 * @component
 * @param {Object} props - The properties passed to the component.
 * @param {Array<Array<number>>} props.board - The game board matrix where each cell indicates the type of cell.
 * @param {{ x: number, y: number }} props.pacmanPosition - The current position of Pacman on the board.
 * @param {{ x: number, y: number }} props.ghostPosition - The current position of the ghost on the board.
 */
const Board = ({ board = [], pacmanPosition, ghostPosition }) => {
  /**
   * Reference to the canvas element.
   * @type {React.RefObject<HTMLCanvasElement>}
   */
  const canvasRef = useRef(null);

  /**
   * State to manage the Pacman image.
   * @type {HTMLImageElement | null}
   */
  const [pacmanImg, setPacmanImg] = useState(null);

  /**
   * State to manage the ghost image.
   * @type {HTMLImageElement | null}
   */
  const [ghostImg, setGhostImg] = useState(null); // State for ghost image

  /**
   * Loads and sets the images for Pacman and the ghost.
   * <p>
   * This effect runs once when the component mounts, loading the images into state.
   * </p>
   */
  useEffect(() => {
    const imgPacman = new Image();
    imgPacman.src = pacmanImageSrc;
    imgPacman.onload = () => setPacmanImg(imgPacman);

    const imgGhost = new Image();
    imgGhost.src = ghostImageSrc; // Load ghost image
    imgGhost.onload = () => setGhostImg(imgGhost);
  }, []);

  /**
   * Draws the board, Pacman, and ghost on the canvas.
   * <p>
   * This effect runs whenever the board, Pacman position, ghost position, or images change.
   * </p>
   */
  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");

    const drawBoard = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      board.forEach((row, rowIndex) => {
        row.forEach((cell, cellIndex) => {
          let cellColor = "#fff";
          if (cell === 1) {
            cellColor = "#00f"; // Color for walls
          } else if (cell === 2) {
            cellColor = "#eebbbb"; // Color for other elements
          }
          ctx.fillStyle = cellColor;
          ctx.fillRect(cellIndex * 30, rowIndex * 30, 30, 30); // Draw cell
        });
      });

      if (pacmanImg) {
        ctx.drawImage(
          pacmanImg,
          pacmanPosition.y * 30, 
          pacmanPosition.x * 30, 
          30, 
          30 
        ); // Draw Pacman
      }

      if (ghostImg) { // Draw the ghost
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
