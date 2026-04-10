package com.ranafahad.unifriends.onboarding;

import com.ranafahad.unifriends.onboarding.dto.OnboardingRequest;
import com.ranafahad.unifriends.onboarding.dto.UsernameCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @GetMapping("/username/check")
    public ResponseEntity<UsernameCheckResponse> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(onboardingService.checkUsername(username));
    }

    @PostMapping("/complete")
    public ResponseEntity<Map<String, String>> completeOnboarding(
            @Valid @RequestBody OnboardingRequest request,
            Principal principal) {
        String token = onboardingService.completeOnboarding(principal.getName(), request);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
