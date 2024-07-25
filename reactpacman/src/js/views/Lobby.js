import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useWebSocket } from '../WebSocketContext';
import loginTitle from "../../assets/images/title.png";
import "./Lobby.css";
import Game from './Game.jsx';

const Lobby = () => {
  const [players, setPlayers] = useState([]);
  const [isReady, setIsReady] = useState(false);
  const [gameStarted, setGameStarted] = useState(false);
  const [initialMatrix, setInitialMatrix] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();
  const { playerData } = location.state;
  const { socket } = useWebSocket();

  useEffect(() => {
    if (!playerData || !socket) {
      navigate('/');
      return;
    }

    socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.type === 'UPDATE_PLAYERS') {
        setPlayers(data.players);
      } else if (data.type === 'START_GAME') {
        setInitialMatrix(data.matrix);
        setGameStarted(true);
      }
    };

    return () => {
      if (socket) {
        socket.onmessage = null;
      }
    };
  }, [navigate, playerData, socket]);

  const handleReady = () => {
    setIsReady(true);
    if (socket) {
      socket.send(JSON.stringify({ type: 'PLAYER_READY' }));
    }
  };

  const handleLogout = async () => {
    try {
      await fetch('/logout', {
        method: 'POST',
        credentials: 'include', // Asegúrate de enviar las cookies de sesión si es necesario
      });

      navigate('/');
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  return (
    <div className="d-flex vh-100">
      <div className="d-flex flex-column w-75">
        <div id="Game" className="flex-grow-1 bg-light">
          {gameStarted && initialMatrix && playerData && players && (
            <Game initialMatrix={initialMatrix} currentPlayer={playerData} players={players} />
          )}
        </div>

      </div>
      <div className="d-flex flex-column w-25 p-3 dere">
        <nav className="navbar navbar-expand-lg mb-3">
          <div className="container-fluid">
            <img src={loginTitle} alt="Pac-Man" className="img-fluid" style={{ maxWidth: '50%', height: 'auto' }} />
            <div className="d-flex justify-content-end">
              <button onClick={handleLogout} className="btn btn-danger">Log out</button>
            </div>
          </div>
        </nav>

        <div className="d-flex flex-column align-items-center flex-grow-1">
          <div className="container d-flex flex-column align-items-center">
            <table className="table table-hover table-striped-columns table-bordered border-primary">
              <thead className="table-dark">
                <tr>
                  <th>Name</th>
                  <th>Type</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {players.length > 0 ? (
                  players.map((player, index) => (
                    <tr key={index}>
                      <td>{player.name}</td>
                      <td>{player.isThief ? 'Ghost' : 'Pacman'}</td>
                      <td>{player.isReady ? '✔️ Ready' : '❌ Not Ready'}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="3">No players available</td>
                  </tr>
                )}
              </tbody>
            </table>
            <button onClick={handleReady} disabled={isReady} className="btn btn-lg btn-dark w-100 fs-6">Ready</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Lobby;
