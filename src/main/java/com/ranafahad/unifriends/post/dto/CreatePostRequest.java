package com.ranafahad.unifriends.post.dto;

import com.ranafahad.unifriends.league.LeagueType;
import java.util.List;

public record CreatePostRequest(String title, String content, LeagueType type, List<String> tags) {
}
