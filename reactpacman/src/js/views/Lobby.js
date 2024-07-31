import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useWebSocket } from '../WebSocketContext';
import loginTitle from "../../assets/images/title.png";
import "./Lobby.css";
import Game from './Game';

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
    // Verifica si los datos del jugador o el socket no están disponibles
    if (!playerData || !socket) {
      navigate('/');
      return;
    }

    // Maneja los mensajes recibidos del socket
    const handleSocketMessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.type === 'UPDATE_PLAYERS') {
        setPlayers(data.players);
      } else if (data.type === 'START_GAME') {
        console.log("llega a looby", data.matrix);
        setInitialMatrix(data.matrix);
        setGameStarted(true);
      } else if (data.type === 'UPDATE_GAME_STATE') {
        console.log("Actualización del estado del juego recibida", data);
        setInitialMatrix(data.matrix);
      }
    };

    socket.addEventListener('message', handleSocketMessage);

    // Limpia el manejador de mensajes cuando el componente se desmonta
    return () => {
      socket.removeEventListener('message', handleSocketMessage);
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
        credentials: 'include',
      });
      navigate('/');
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  return (
    <div className="d-flex vh-100">
      <div className="d-flex w-75">
        <div className="bg-dark justify-content-center flex-column flex-grow-1 bg-light">
          <Game matrix={initialMatrix} />
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
