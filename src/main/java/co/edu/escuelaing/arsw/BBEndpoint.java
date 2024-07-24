package co.edu.escuelaing.arsw;

import java.io.IOException;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WebSocket endpoint for handling drawing actions via WebSocket protocol.
 * This component manages WebSocket sessions, message processing, and error
 * handling.
 */
@Component
@ServerEndpoint("/bbService")
public class BBEndpoint {
    private static final Logger logger = Logger.getLogger(BBEndpoint.class.getName());
    /* Queue for all open WebSocket sessions */
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();
    Session ownSession = null;
    private boolean accepted = false;
    TicketRepository ticketRepo = (TicketRepository) BBApplicationContextAware.getApplicationContext().getBean("ticketRepository");
    // Map to store tickets with their corresponding sessions
    private static Map<String, Session> ticketSessionMap = new HashMap<>();
    
    /**
     * Sends a message to all connected WebSocket clients except the sender.
     *
     * @param msg The message to send.
     */
    public void send(String msg) {
        try {
            for (Session session : queue) {
                session.getBasicRemote().sendText(msg);
                logger.log(Level.INFO, "Sent: {0}", msg);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, "Error sending message: {0}", e.getMessage());
        }
    }

    /**
     * Handles incoming messages from WebSocket clients.
     *
     * @param message The incoming message.
     * @param session The session from which the message originated.
     */
    @OnMessage
    public void processPoint(String message, Session session) {
        if (accepted) {
            this.send(message);
        } else {
            if (!accepted && ticketRepo.checkTicket(message)) {
                accepted = true;
            } else {
                try {
                    ownSession.close();
                } catch (IOException ex) {
                    Logger.getLogger(BBEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Handles opening of WebSocket connections.
     *
     * @param session The newly opened WebSocket session.
     */
    @OnOpen
    public void openConnection(Session session) {
        logger.log(Level.INFO, "Connection opened. Session ID: {0}", session.getId());
        queue.add(session);
    }

    /**
     * Handles closure of WebSocket connections.
     *
     * @param session The closed WebSocket session.
     */
    @OnClose
    public void closedConnection(Session session) {
        logger.log(Level.INFO, "Connection closed. Session ID: {0}", session.getId());
        queue.remove(session);
        // Remove ticket from map if exists
        ticketSessionMap.values().removeIf(s -> s.getId().equals(session.getId()));
    }

    /**
     * Handles WebSocket errors.
     *
     * @param session The session in which the error occurred.
     * @param t       The Throwable representing the error.
     */
    @OnError
    public void error(Session session, Throwable t) {
        logger.log(Level.INFO, "Connection error. Session ID: {0}, Error: {1}",
                new Object[] { session.getId(), t.getMessage() });
        queue.remove(session);
        // Remove ticket from map if exists
        ticketSessionMap.values().removeIf(s -> s.getId().equals(session.getId()));
    }

}
