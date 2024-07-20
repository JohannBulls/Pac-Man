function startAudioContext() {
  if (p5.soundOut.audioContext.state === "suspended") {
    p5.soundOut.audioContext.resume();
  }
}

function BBServiceURL() {
  const protocol = window.location.protocol === "https:" ? "wss:" : "ws:";
  const host = window.location.host;
  const url = `${protocol}//${host}/bbService`;
  console.log("BBService URL Calculada: " + url);
  return url;
}

function ticketServiceURL() {
  const protocol = window.location.protocol;
  const host = window.location.host;
  const url = `${protocol}//${host}/getticket`;
  console.log("ticketService URL Calculada: " + url);
  return url;
}

async function getTicket() {
  try {
    const response = await fetch(ticketServiceURL());
    if (response.ok) {
      const json = await response.json();
      return json;
    } else {
      console.log("HTTP-Error: " + response.status);
    }
  } catch (error) {
    console.error("Error fetching ticket:", error);
  }
  return null;
}

class WSBBChannel {
  constructor(URL, callback) {
    this.URL = URL;
    this.wsocket = new WebSocket(URL);
    this.wsocket.onopen = (evt) => this.onOpen(evt);
    this.wsocket.onmessage = (evt) => this.onMessage(evt);
    this.wsocket.onerror = (evt) => this.onError(evt);
    this.receivef = callback;
  }

  async onOpen(evt) {
    console.log("In onOpen", evt);
    const json = await getTicket();
    if (json) {
      this.wsocket.send(json.ticket);
    }
  }

  onMessage(evt) {
    console.log("In onMessage", evt);
    if (evt.data !== "Connection established.") {
      this.receivef(evt.data);
    }
  }

  onError(evt) {
    console.error("In onError", evt);
  }

  send(x, y) {
    const msg = JSON.stringify({ x, y });
    console.log("sending: ", msg);
    this.wsocket.send(msg);
  }

  close() {
    this.wsocket.close();
  }
}

function BBCanvas() {
  const [svrStatus, setSvrStatus] = React.useState({ loadingState: "Loading Canvas..." });
  const comunicationWS = React.useRef(null);
  const myp5 = React.useRef(null);

  const sketch = (p) => {
    p.setup = () => {
      p.createCanvas(700, 410);
    };

    p.draw = () => {
      if (p.mouseIsPressed) {
        p.fill(0, 0, 0);
        p.ellipse(p.mouseX, p.mouseY, 20, 20);
        comunicationWS.current.send(p.mouseX, p.mouseY);
      } else {
        p.fill(255, 255, 255);
      }
    };
  };

  React.useEffect(() => {
    myp5.current = new p5(sketch, "container");
    setSvrStatus({ loadingState: "Canvas Loaded" });
    comunicationWS.current = new WSBBChannel(BBServiceURL(), (msg) => {
      const obj = JSON.parse(msg);
      console.log("On func call back ", msg);
      drawPoint(obj.x, obj.y);
    });

    return () => {
      console.log("Closing connection ...");
      comunicationWS.current.close();
    };
  }, []);

  const drawPoint = (x, y) => {
    myp5.current.ellipse(x, y, 20, 20);
  };

  return (
    <div>
      <h4>Drawing status: {svrStatus.loadingState}</h4>
      <div id="container"></div>
    </div>
  );
}

function Editor({ name }) {
  return (
    <div className="container mt-4">
      <hr />
      <h1 className="text-center">Hello, {name}</h1>
      <hr />
      <div className="row">
        <div className="col">
          <BBCanvas />
        </div>
      </div>
      <hr />
    </div>
  );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<Editor name="Johann Amaya" />);
