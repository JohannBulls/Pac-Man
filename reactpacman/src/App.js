import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Board from './Board';

function App() {
  return (
    <div className="App vh-100 overflow-hidden">
      <header className="App-header text-center bg-dark text-light py-3">
        <div className="container">
          <h1 className="display-5">¡Bienvenido a Pacman!</h1>
        </div>
      </header>
      <main className="py-4 overflow-auto" style={{ maxHeight: 'calc(100vh - 120px)' }}>
        <div className="container">
          <Board />
        </div>
      </main>
    </div>
  );
}

export default App;
