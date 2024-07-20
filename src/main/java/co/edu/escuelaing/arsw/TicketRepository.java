package co.edu.escuelaing.arsw;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class TicketRepository {

    @Autowired
    private StringRedisTemplate template;

    @Resource(name = "stringRedisTemplate")
    private ListOperations<String, String> listTickets;

    private int ticketnumber;

    /**
     * Constructs a TicketRepository instance.
     */
    public TicketRepository() {
    }

    /**
     * Retrieves the next ticket number and stores it in Redis.
     *
     * @return The next ticket number.
     */
    public synchronized Integer getTicket() {
        Integer a = ticketnumber++;
        listTickets.leftPush("ticketStore", a.toString());
        return a;
    }

    /**
     * Checks if a ticket is valid and removes it from Redis.
     *
     * @param ticket Ticket to check.
     * @return True if the ticket is valid; false otherwise.
     */
    public boolean checkTicket(String ticket) {
        Long isValid = listTickets.getOperations().boundListOps("ticketStore").remove(0, ticket);
        return (isValid > 0L);
    }

    private void eviction() {
        // Delete tickets after timeout or include this functionality in checkTicket
    }
}