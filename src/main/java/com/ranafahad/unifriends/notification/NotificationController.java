package com.ranafahad.unifriends.notification;

import com.ranafahad.unifriends.notification.dto.UnreadCountResponse;
import com.ranafahad.unifriends.user.User;
import com.ranafahad.unifriends.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/counts")
    public ResponseEntity<List<UnreadCountResponse>> getCounts(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<UnreadCount> counts = notificationService.getUnreadCounts(user.getId());
        return ResponseEntity.ok(counts.stream().map(UnreadCountResponse::from).toList());
    }
}
