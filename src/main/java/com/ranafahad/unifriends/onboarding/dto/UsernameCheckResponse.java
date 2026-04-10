package com.ranafahad.unifriends.onboarding.dto;

public record UsernameCheckResponse(
        boolean available,
        boolean profanityFlagged
) {
}
