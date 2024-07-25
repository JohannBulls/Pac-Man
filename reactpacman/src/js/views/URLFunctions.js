export function WShostURL() {
    var host = window.location.hostname;
    var url = 'ws://' + host + ':8080' + '/lobby';
    console.log("WebSocket host URL Calculada: " + url);
    return url;
  }
  
  export function RESThostURL() {
    var host = window.location.hostname;
    var protocol = window.location.protocol;
    var url = protocol + '//' + host + ':8080';
    console.log("REST host URL Calculada: " + url);
    return url;
  }