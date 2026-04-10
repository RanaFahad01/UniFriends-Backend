package com.ranafahad.unifriends.admin;

import com.ranafahad.unifriends.admin.dto.BanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users/{id}/ban")
    public ResponseEntity<Void> banUser(
            @PathVariable Long id,
            @RequestBody BanRequest request,
            Principal principal) {
        adminService.banUser(id, request.reason(), principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}/ban")
    public ResponseEntity<Void> unbanUser(@PathVariable Long id, Principal principal) {
        adminService.unbanUser(id, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/make-moderator")
    public ResponseEntity<Void> makeModerator(@PathVariable Long id, Principal principal) {
        adminService.makeModerator(id, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}/make-moderator")
    public ResponseEntity<Void> removeModerator(@PathVariable Long id, Principal principal) {
        adminService.removeModerator(id, principal.getName());
        return ResponseEntity.ok().build();
    }
}
