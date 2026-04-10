package com.ranafahad.unifriends.post.dto;

import com.ranafahad.unifriends.post.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long authorId,
        String authorUsername,
        String authorAvatarUrl,
        String content,
        String type,
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getAvatarUrl(),
                post.getContent(),
                post.getType().name(),
                post.getCreatedAt()
        );
    }
}
