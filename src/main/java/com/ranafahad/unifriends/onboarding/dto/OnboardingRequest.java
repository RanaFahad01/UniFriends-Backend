package com.ranafahad.unifriends.onboarding.dto;

public record OnboardingRequest(
        String username,
        ProfileSection academic,    // required — must not be null
        ProfileSection personality  // required — must not be null
) {
}
