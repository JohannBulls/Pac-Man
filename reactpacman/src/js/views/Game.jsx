import React, { useRef, useState, useEffect } from 'react';
import p5 from 'p5';
import ReactDOM from 'react-dom/client'; // Asegúrate de usar esta importación si usas la versión 18 o superior de React

function BBCanvas() {
  const canvasRef = useRef(null);
  const [board, setBoard] = useState(null);

  useEffect(() => {
    fetch('/api/pacman/generateMatrix')
      .then(response => response.json())
      .then(data => setBoard(data))
      .catch(error => console.error('Error fetching board:', error));
  }, []);

  useEffect(() => {
    if (!board) return;

    const sketch = (p) => {
      const cellSize = 40;

      p.setup = () => {
        p.createCanvas(600, 400).parent(canvasRef.current);
      };

      p.draw = () => {
        p.background(0);
        drawBoard();
      };

      function drawBoard() {
        for (let i = 0; i < board.length; i++) {
          for (let j = 0; j < board[i].length; j++) {
            const color = board[i][j] === 1 ? 'blue' : 'white';
            p.fill(color);
            p.noStroke();
            p.rect(j * cellSize, i * cellSize, cellSize, cellSize);
          }
        }
      }
    };

    const canvas = new p5(sketch);

    return () => {
      canvas.remove();
    };
  }, [board]);

  return <div ref={canvasRef}></div>;
}

function Editor() {
  return <BBCanvas />;
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<Editor />);

export default BBCanvas;
