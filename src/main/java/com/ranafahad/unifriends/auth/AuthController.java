package com.ranafahad.unifriends.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final WsTicketStore wsTicketStore;

    @Value("${app.cookie.secure:true}")
    private boolean secureCookie;

    /**
     * Issues a short-lived, one-time WebSocket ticket for the authenticated user.
     * The frontend calls this just before opening a STOMP connection and passes
     * the returned ticket as the Authorization header in the STOMP CONNECT frame.
     */
    @GetMapping("/ws-ticket")
    public ResponseEntity<Map<String, String>> wsTicket(Principal principal) {
        String ticket = UUID.randomUUID().toString();
        wsTicketStore.store(ticket, principal.getName());
        return ResponseEntity.ok(Map.of("ticket", ticket));
    }

    /**
     * Clears both auth cookies, effectively logging the user out.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie clearJwt = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie clearPresence = ResponseCookie.from("jwt_present", "")
                .httpOnly(false)
                .secure(secureCookie)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearJwt.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearPresence.toString());

        return ResponseEntity.ok().build();
    }
}
