package com.ranafahad.unifriends.onboarding.dto;

import java.util.List;

public record ProfileSection(
        String bio,
        List<String> tags,
        List<String> hobbies
) {
}
