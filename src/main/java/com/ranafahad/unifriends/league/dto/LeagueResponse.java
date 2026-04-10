package com.ranafahad.unifriends.league.dto;

import com.ranafahad.unifriends.league.League;

import java.time.LocalDateTime;

public record LeagueResponse(
        Long id,
        String name,
        String type,
        String description,
        int memberCount,
        LocalDateTime createdAt
) {
    public static LeagueResponse from(League league) {
        return new LeagueResponse(
                league.getId(),
                league.getName(),
                league.getType().name(),
                league.getDescription(),
                league.getMembers().size(),
                league.getCreatedAt()
        );
    }
}
