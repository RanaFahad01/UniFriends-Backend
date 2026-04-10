package com.ranafahad.unifriends.auth;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class WsTicketStore {

    private final ConcurrentHashMap<String, String> tickets = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void store(String ticket, String email) {
        tickets.put(ticket, email);
        scheduler.schedule(() -> tickets.remove(ticket), 30, TimeUnit.SECONDS);
    }

    /** Returns the email and removes the ticket (one-time use). */
    public String redeem(String ticket) {
        return tickets.remove(ticket);
    }
}
