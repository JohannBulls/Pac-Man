// // function startAudioContext() {
// //     if (p5.soundOut.audioContext.state === "suspended") {
// //       p5.soundOut.audioContext.resume();
// //     }
// //   }
  
// //   function BBServiceURL() {
// //     const protocol = window.location.protocol === "https:" ? "wss:" : "ws:";
// //     const host = window.location.host;
// //     const url = `${protocol}//${host}/bbService`;
// //     console.log("BBService URL Calculada: " + url);
// //     return url;
// //   }
  
// //   function ticketServiceURL() {
// //     const protocol = window.location.protocol;
// //     const host = window.location.host;
// //     const url = `${protocol}//${host}/getticket`;
// //     console.log("ticketService URL Calculada: " + url);
// //     return url;
// //   }
  
// //   async function getTicket() {
// //     try {
// //       const response = await fetch(ticketServiceURL());
// //       if (response.ok) {
// //         const json = await response.json();
// //         return json;
// //       } else {
// //         console.log("HTTP-Error: " + response.status);
// //       }
// //     } catch (error) {
// //       console.error("Error fetching ticket:", error);
// //     }
// //     return null;
// //   }
  
// //   class WSBBChannel {
// //     constructor(URL, callback) {
// //       this.URL = URL;
// //       this.wsocket = new WebSocket(URL);
// //       this.wsocket.onopen = (evt) => this.onOpen(evt);
// //       this.wsocket.onmessage = (evt) => this.onMessage(evt);
// //       this.wsocket.onerror = (evt) => this.onError(evt);
// //       this.receivef = callback;
// //     }
  
// //     async onOpen(evt) {
// //       console.log("In onOpen", evt);
// //       const json = await getTicket();
// //       if (json) {
// //         this.wsocket.send(json.ticket);
// //       }
// //     }
  
// //     onMessage(evt) {
// //       console.log("In onMessage", evt);
// //       if (evt.data !== "Connection established.") {
// //         this.receivef(evt.data);
// //       }
// //     }
  
// //     onError(evt) {
// //       console.error("In onError", evt);
// //     }
  
// //     send(x, y) {
// //       const msg = JSON.stringify({ x, y });
// //       console.log("sending: ", msg);
// //       this.wsocket.send(msg);
// //     }
  
// //     close() {
// //       this.wsocket.close();
// //     }
// //   }
  
// //   function BBCanvas() {
// //     const [svrStatus, setSvrStatus] = React.useState({ loadingState: "Loading Canvas..." });
// //     const comunicationWS = React.useRef(null);
// //     const myp5 = React.useRef(null);
  
// //     const sketch = (p) => {
// //       p.setup = () => {
// //         p.createCanvas(700, 410);
// //       };
  
// //       p.draw = () => {
// //         if (p.mouseIsPressed) {
// //           p.fill(0, 0, 0);
// //           p.ellipse(p.mouseX, p.mouseY, 20, 20);
// //           comunicationWS.current.send(p.mouseX, p.mouseY);
// //         } else {
// //           p.fill(255, 255, 255);
// //         }
// //       };
// //     };
  
// //     React.useEffect(() => {
// //       myp5.current = new p5(sketch, "container");
// //       setSvrStatus({ loadingState: "Canvas Loaded" });
// //       comunicationWS.current = new WSBBChannel(BBServiceURL(), (msg) => {
// //         const obj = JSON.parse(msg);
// //         console.log("On func call back ", msg);
// //         drawPoint(obj.x, obj.y);
// //       });
  
// //       return () => {
// //         console.log("Closing connection ...");
// //         comunicationWS.current.close();
// //       };
// //     }, []);
  
// //     const drawPoint = (x, y) => {
// //       myp5.current.ellipse(x, y, 20, 20);
// //     };
  
// //     return (
// //       <div>
// //         <h4>Drawing status: {svrStatus.loadingState}</h4>
// //         <div id="container"></div>
// //       </div>
// //     );
// //   }
  










//   import React, { useState, useEffect, useCallback } from "react";
// // import "bootstrap/dist/css/bootstrap.min.css";
// import Board from "./Board";
// import "../css/App.css";
// import musicFile from "../assets/sounds/music.mp3";

// function BBCanvas  ()  {
//   const initialBoard = [
//     [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
//     [1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1],
//     [1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1],
//     [1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1],
//     [1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1],
//     [1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1],
//     [1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1],
//     [1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1],
//     [0, 0, 0, 0, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 0, 0, 0, 0],
//     [1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1],
//     [2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2],
//     [1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1],
//     [0, 0, 0, 0, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 0, 0, 0, 0],
//     [0, 0, 0, 0, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 0, 0, 0, 0],
//     [1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1],
//     [1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1],
//     [1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1],
//     [1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1],
//     [1, 1, 2, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 2, 1, 1],
//     [1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1],
//     [1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1],
//     [1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1],
//     [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
//   ];

//   const [music] = useState(new Audio(musicFile));
//   const [pacmanPosition, setPacmanPosition] = useState({ x: 13, y: 10 });

//   const startGame = () => {
//     music.play();
//     console.log("Game started");
//   };

//   const handleKeyDown = useCallback(
//     async (event) => {
//       let newX = pacmanPosition.x;
//       let newY = pacmanPosition.y;
  
//       switch (event.key) {
//         case "w":
//           newX = Math.max(pacmanPosition.x - 2, 0);
//           break;
//         case "s":
//           newX = Math.min(pacmanPosition.x + 2, initialBoard.length - 1);
//           break;
//         case "a":
//           newY = Math.max(pacmanPosition.y - 2, 0);
//           break;
//         case "d":
//           newY = Math.min(pacmanPosition.y + 2, initialBoard[0].length - 1);
//           break;
//         default:
//           return;
//       }
  
//       try {
//         const response = await fetch("http://localhost:8080/api/move", {
//           method: "POST",
//           headers: {
//             "Content-Type": "application/json",
//           },
//           body: JSON.stringify({ direction: event.key }),
//         });
  
//         if (!response.ok) {
//           throw new Error(`HTTP error! Status: ${response.status}`);
//         }
  
//         const result = await response.json();
//         setPacmanPosition({ x: newX, y: newY });
//       } catch (error) {
//         console.error("Error moving Pacman:", error);
//       }
//     },
//     [pacmanPosition, initialBoard]
//   );

//   useEffect(() => {
//     window.addEventListener("keydown", handleKeyDown);
//     return () => {
//       window.removeEventListener("keydown", handleKeyDown);
//     };
//   }, [handleKeyDown]);

//   return (
//     <div className="App vh-100 overflow-hidden">
//       <div className="container-fluid h-100">
//         <div className="row h-100">
//           <div className="col-lg-9 p-0">
//             <div className="h-100 d-flex justify-content-center align-items-center">
//               <Board board={initialBoard} pacmanPosition={pacmanPosition} />
//             </div>
//           </div>
//           <div className="col-lg-3 bg-light p-0">
//             <div className="p-4">
//               <header className="App-header text-center bg-dark text-light py-3">
//                 <div className="container">
//                   <h1 className="display-5">PACMAN GAME</h1>
//                   <button className="btn btn-primary" onClick={startGame}>
//                     Start Game
//                   </button>
//                 </div>
//               </header>
//             </div>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };









//   function Editor({ name }) {
//     return (
//       <div className="container mt-4">
//         <hr />
//         <h1 className="text-center">Hello, {name}</h1>
//         <hr />
//         <div className="row">
//           <div className="col">
//             <BBCanvas />
//           </div>
//         </div>
//         <hr />
//       </div>
//     );
//   }
  
//   const root = ReactDOM.createRoot(document.getElementById("root"));
//   root.render(<Editor name="Johann Amaya" />);
  