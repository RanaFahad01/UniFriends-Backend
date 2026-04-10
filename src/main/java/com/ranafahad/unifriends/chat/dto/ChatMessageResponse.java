package com.ranafahad.unifriends.chat.dto;

import com.ranafahad.unifriends.chat.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long id,
        Long leagueId,
        Long senderId,
        String senderUsername,
        String senderAvatarUrl,
        String content,
        LocalDateTime sentAt
) {
    public static ChatMessageResponse from(ChatMessage msg) {
        return new ChatMessageResponse(
                msg.getId(),
                msg.getLeague().getId(),
                msg.getSender().getId(),
                msg.getSender().getUsername(),
                msg.getSender().getAvatarUrl(),
                msg.getContent(),
                msg.getSentAt()
        );
    }
}
