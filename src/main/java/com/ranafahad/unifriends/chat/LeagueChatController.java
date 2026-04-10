package com.ranafahad.unifriends.chat;

import com.ranafahad.unifriends.chat.dto.ChatMessageResponse;
import com.ranafahad.unifriends.chat.dto.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class LeagueChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/api/leagues/{id}/messages")
    public ResponseEntity<Page<ChatMessageResponse>> getMessages(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            Principal principal) {
        Page<ChatMessage> messages = chatService.getMessages(id, principal.getName(), PageRequest.of(page, 50));
        return ResponseEntity.ok(messages.map(ChatMessageResponse::from));
    }

    @PostMapping("/api/leagues/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id, Principal principal) {
        chatService.markRead(id, principal.getName());
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/league/{leagueId}/send")
    public void sendMessage(
            @DestinationVariable Long leagueId,
            @Payload SendMessageRequest request,
            Principal principal) {
        ChatMessage message = chatService.sendMessage(leagueId, principal.getName(), request.content());
        messagingTemplate.convertAndSend("/topic/league/" + leagueId, ChatMessageResponse.from(message));
    }
}
