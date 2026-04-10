package com.ranafahad.unifriends.user.dto;

import com.ranafahad.unifriends.user.User;

public record UserResponse(
        Long id,
        String email,
        String username,
        String avatarUrl,
        String role,
        boolean isNewUser
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getAvatarUrl(),
                user.getRole().name(),
                user.isNewUser()
        );
    }
}
