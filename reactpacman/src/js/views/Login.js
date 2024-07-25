import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useWebSocket } from "../WebSocketContext";
import loginImage from "../../assets/images/pacman.gif";
import loginTitle from "../../assets/images/title.png";
import "./Login.css";
import musiclogin from "../../assets/sounds/background.mp3";
import { RESThostURL, WShostURL } from "./URLFunctions"; // Importa las funciones

const Login = () => {
    const [name, setName] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const { setSocket } = useWebSocket();

    const handleNameChange = (e) => {
        const inputValue = e.target.value;
        // Solo permite letras y espacios
        const filteredValue = inputValue.replace(/[^a-zA-Z\s]/g, "");
        setName(filteredValue);
    };

    const handlePlay = async () => {
        if (!name.trim()) {
            setError("Name is required");
            return;
        }

        // Randomly choose between Pacman and Ghost
        const isThief = Math.random() < 0.7; // 50% chance for each

        try {
            const response = await fetch(
                `${RESThostURL()}/login/getPlayerData?isThief=${isThief}`
            );
            if (!response.ok) {
                throw new Error("No available positions");
            }

            const playerData = await response.json();
            playerData.name = name.trim();
            playerData.isThief = isThief;

            const socket = new WebSocket(WShostURL());
            socket.onopen = () => {
                console.log("WebSocket connection established");
                socket.send(JSON.stringify({ type: "JOIN", ...playerData }));
            };

            setSocket(socket);
            navigate("/lobby", { state: { playerData: playerData } });
        } catch (error) {
            setError("Failed to get player data: " + error.message);
        }
    };

    return (
        <div>
            <div>
                <audio autoPlay loop>
                    <source src={musiclogin} type="audio/mpeg" />
                    Tu navegador no soporta el elemento de audio.
                </audio>
            </div>
            {error && <div className="alert alert-danger text-center">{error}</div>}

            <div className="container d-flex justify-content-center align-items-center min-vh-100">
                <div className="row border rounded-5 p-3 bg-white shadow box-area">
                    <div
                        className="col-md-6 rounded-4 d-flex justify-content-center align-items-center flex-column left-box"
                        style={{ background: "#000" }}
                    >
                        <div className="featured-image mb-3">
                            <img
                                src={loginImage}
                                className="img-fluid"
                                style={{ width: "250px" }}
                                alt="Pacman"
                            />
                        </div>
                        <img
                            src={loginTitle}
                            alt="Pac-Man"
                            className="img-fluid"
                            style={{ maxWidth: "50%", height: "auto" }}
                        />
                        <small
                            className="text-white text-wrap text-center"
                            style={{
                                width: "17rem",
                                fontFamily: "'Courier New', Courier, monospace",
                            }}
                        >
                            Real-time Multiplayer
                        </small>
                    </div>
                    <div className="col-md-6 right-box">
                        <div className="row align-items-center">
                            <div className="header-text mb-4">
                                <h2>Login</h2>
                                <p>We are happy to have you back.</p>
                            </div>
                            <div className="input-group mb-3">
                                <input
                                    type="text"
                                    className="form-control form-control-lg bg-light fs-6"
                                    placeholder="Enter your name"
                                    value={name}
                                    onChange={handleNameChange}
                                />
                            </div>
                            <div className="input-group mb-3">
                                <button
                                    onClick={handlePlay}
                                    className="btn btn-lg btn-dark w-100 fs-6"
                                >
                                    Play
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
