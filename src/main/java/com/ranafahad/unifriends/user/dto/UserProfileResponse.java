package com.ranafahad.unifriends.user.dto;

import com.ranafahad.unifriends.profile.Profile;
import com.ranafahad.unifriends.user.User;

public record UserProfileResponse(
        Long id,
        Long userId,
        String username,
        String avatarUrl,
        String role,
        String type,
        String bio,
        String tags,
        String hobbies
) {
    public static UserProfileResponse from(User user, Profile profile) {
        return new UserProfileResponse(
                profile.getId(),
                user.getId(),
                user.getUsername(),
                user.getAvatarUrl(),
                user.getRole().name(),
                profile.getType().name(),
                profile.getBio(),
                profile.getTags(),
                profile.getHobbies()
        );
    }
}
