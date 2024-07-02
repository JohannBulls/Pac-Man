// App.js

import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import Board from "./Board";

function App() {
  return (
    <div className="App vh-100 overflow-hidden d-flex justify-content-center align-items-center">
      <div className="container-fluid p-0">
        <div className="row m-0">
          <div className="col-lg-9 p-0">
            <div className="p-4">
              <div className="container">
                <Board />
              </div>
            </div>
          </div>
          <div className="col-lg-3 bg-light p-0">
            <div className="p-4">
              <header className="App-header text-center bg-dark text-light py-3">
                <div className="container">
                  <h1 className="display-5">PACMAN GAME</h1>
                </div>
              </header>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
