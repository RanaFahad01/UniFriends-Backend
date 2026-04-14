package com.ranafahad.unifriends.user;

import com.ranafahad.unifriends.profile.Profile;
import com.ranafahad.unifriends.profile.ProfileService;
import com.ranafahad.unifriends.profile.ProfileType;
import com.ranafahad.unifriends.report.ReportService;
import com.ranafahad.unifriends.report.dto.ReportRequest;
import com.ranafahad.unifriends.user.dto.UserProfileResponse;
import com.ranafahad.unifriends.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReportService reportService;
    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @PathVariable Long userId,
            @RequestParam ProfileType type) {
        User user = userService.findById(userId);
        Profile profile = profileService.findByUserIdAndType(userId, type);
        return ResponseEntity.ok(UserProfileResponse.from(user, profile));
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<Void> reportUser(
            @PathVariable Long id,
            @RequestBody ReportRequest request,
            Principal principal) {
        reportService.createReport(principal.getName(), id, request.reason());
        return ResponseEntity.ok().build();
    }
}
