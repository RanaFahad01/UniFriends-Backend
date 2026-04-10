package com.ranafahad.unifriends.profile.dto;

import com.ranafahad.unifriends.profile.Profile;

public record ProfileResponse(
        Long id,
        Long userId,
        String type,
        String bio,
        String tags,
        String hobbies
) {
    public static ProfileResponse from(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getType().name(),
                profile.getBio(),
                profile.getTags(),
                profile.getHobbies()
        );
    }
}
