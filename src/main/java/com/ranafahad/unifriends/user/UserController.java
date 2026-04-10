package com.ranafahad.unifriends.user;

import com.ranafahad.unifriends.report.ReportService;
import com.ranafahad.unifriends.report.dto.ReportRequest;
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

    @PostMapping("/{id}/report")
    public ResponseEntity<Void> reportUser(
            @PathVariable Long id,
            @RequestBody ReportRequest request,
            Principal principal) {
        reportService.createReport(principal.getName(), id, request.reason());
        return ResponseEntity.ok().build();
    }
}
