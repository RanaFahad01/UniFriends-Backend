package com.ranafahad.unifriends.onboarding;

import com.ranafahad.unifriends.auth.JwtService;
import com.ranafahad.unifriends.onboarding.dto.OnboardingRequest;
import com.ranafahad.unifriends.onboarding.dto.UsernameCheckResponse;
import com.ranafahad.unifriends.profile.Profile;
import com.ranafahad.unifriends.profile.ProfileRepository;
import com.ranafahad.unifriends.profile.ProfileType;
import com.ranafahad.unifriends.user.User;
import com.ranafahad.unifriends.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfanityCheckService profanityCheckService;
    private final JwtService jwtService;

    @Transactional
    public String completeOnboarding(String callerEmail, OnboardingRequest request) {
        User user = userRepository.findByEmail(callerEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Validate username
        String username = request.username();
        validateUsername(username);

        // Validate that both profile sections are provided
        if (request.academic() == null) {
            throw new IllegalStateException("An academic (Student) profile is required to complete onboarding");
        }
        if (request.personality() == null) {
            throw new IllegalStateException("A personality profile is required to complete onboarding");
        }

        // Set username on user
        user.setUsername(username);
        user.setNewUser(false);
        userRepository.save(user);

        // Create both profiles
        Profile studentProfile = Profile.builder()
                .user(user)
                .type(ProfileType.STUDENT)
                .bio(request.academic().bio())
                .tags(joinIfPresent(request.academic().tags()))
                .hobbies(joinIfPresent(request.academic().hobbies()))
                .build();
        profileRepository.save(studentProfile);

        Profile personalityProfile = Profile.builder()
                .user(user)
                .type(ProfileType.PERSONALITY)
                .bio(request.personality().bio())
                .tags(joinIfPresent(request.personality().tags()))
                .hobbies(joinIfPresent(request.personality().hobbies()))
                .build();
        profileRepository.save(personalityProfile);

        // Return fresh JWT with isNewUser = false
        return jwtService.generateToken(user);
    }

    public UsernameCheckResponse checkUsername(String username) {
        boolean formatValid = username != null && username.matches("^[a-zA-Z0-9_]{3,15}$");
        if (!formatValid) {
            return new UsernameCheckResponse(false, false);
        }
        boolean profanityFlagged = profanityCheckService.isFlagged(username);
        boolean exists = userRepository.existsByUsername(username);
        return new UsernameCheckResponse(!exists && !profanityFlagged, profanityFlagged);
    }

    private void validateUsername(String username) {
        if (username == null || !username.matches("^[a-zA-Z0-9_]{3,15}$")) {
            throw new IllegalStateException("Username must be 3-15 alphanumeric characters or underscores");
        }
        if (profanityCheckService.isFlagged(username)) {
            throw new IllegalStateException("Username contains inappropriate content");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username is already taken");
        }
    }

    private String joinIfPresent(java.util.List<String> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        return String.join(",", items);
    }
}
