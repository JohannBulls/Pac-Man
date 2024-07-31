import React, { useEffect } from 'react';
import { initializeGame } from '../game';
import './Game.css';

const Game = ({ matrix }) => {
  console.log("llega a Game =>"+matrix);

  useEffect(() => {
    if (matrix && matrix.length > 0 && matrix[0].length > 0) {
      initializeGame('game-container', matrix.length, matrix[0].length, matrix);
    }
  }, [matrix]);

  return <div id="game-container"></div>;
};

export default Game;
