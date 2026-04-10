package com.ranafahad.unifriends.notification.dto;

import com.ranafahad.unifriends.notification.UnreadCount;

public record UnreadCountResponse(Long leagueId, int count) {
    public static UnreadCountResponse from(UnreadCount uc) {
        return new UnreadCountResponse(uc.getId().getLeagueId(), uc.getCount());
    }
}
