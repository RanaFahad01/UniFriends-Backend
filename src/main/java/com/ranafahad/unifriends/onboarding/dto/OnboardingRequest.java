package com.ranafahad.unifriends.onboarding.dto;

public record OnboardingRequest(
        String username,
        ProfileSection academic,
        ProfileSection personality
) {
}
