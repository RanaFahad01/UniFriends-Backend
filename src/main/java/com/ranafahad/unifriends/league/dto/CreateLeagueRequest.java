package com.ranafahad.unifriends.league.dto;

import com.ranafahad.unifriends.league.LeagueType;

public record CreateLeagueRequest(String name, LeagueType type, String description) {
}
