package com.ranafahad.unifriends.post.dto;

import com.ranafahad.unifriends.post.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long authorId,
        String authorUsername,
        String authorAvatarUrl,
        String title,
        String content,
        String type,
        String tags,           // comma-separated, e.g. "chess,python,math" — split on "," to render pills
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getAvatarUrl(),
                post.getTitle(),
                post.getContent(),
                post.getType().name(),
                post.getTags(),
                post.getCreatedAt()
        );
    }
}
