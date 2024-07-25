import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./views/Login";
import Lobby from "./views/Lobby";
import Game from "./views/Game";
import { WebSocketProvider } from "./WebSocketContext";

function App() {
  return (
    <WebSocketProvider>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/lobby" element={<Lobby />} />
            <Route path="/game" element={<Game />} />
          </Routes>
        </div>
      </Router>
    </WebSocketProvider>
  );
}

export default App;
