package com.ranafahad.unifriends.post.dto;

import com.ranafahad.unifriends.league.LeagueType;

public record CreatePostRequest(String content, LeagueType type) {
}
