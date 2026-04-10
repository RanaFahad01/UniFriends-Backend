package com.ranafahad.unifriends.profile;

import com.ranafahad.unifriends.profile.dto.ProfileResponse;
import com.ranafahad.unifriends.profile.dto.ProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long id) {
        Profile profile = profileService.findById(id);
        return ResponseEntity.ok(ProfileResponse.from(profile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable Long id,
            @RequestBody ProfileUpdateRequest request,
            Principal principal) {
        Profile updated = profileService.updateProfile(id, principal.getName(), request.bio(), request.tags(), request.hobbies());
        return ResponseEntity.ok(ProfileResponse.from(updated));
    }
}
