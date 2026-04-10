package com.ranafahad.unifriends.admin;

import com.ranafahad.unifriends.user.Role;
import com.ranafahad.unifriends.user.User;
import com.ranafahad.unifriends.user.UserRepository;
import com.ranafahad.unifriends.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public void banUser(Long userId, String reason, String callerEmail) {
        User caller = userService.findByEmail(callerEmail);
        User target = userService.findById(userId);

        // ADMIN cannot ban another ADMIN
        if (target.getRole() == Role.ADMIN) {
            throw new IllegalStateException("Cannot ban an admin");
        }

        target.setBannedAt(LocalDateTime.now());
        target.setBanReason(reason);
        userRepository.save(target);
    }

    @Transactional
    public void unbanUser(Long userId, String callerEmail) {
        User target = userService.findById(userId);

        target.setBannedAt(null);
        target.setBanReason(null);
        userRepository.save(target);
    }

    @Transactional
    public void makeModerator(Long userId, String callerEmail) {
        User target = userService.findById(userId);

        target.setRole(Role.MODERATOR);
        userRepository.save(target);
    }

    @Transactional
    public void removeModerator(Long userId, String callerEmail) {
        User target = userService.findById(userId);

        if (target.getRole() != Role.MODERATOR) {
            throw new IllegalStateException("User is not a moderator");
        }

        target.setRole(Role.USER);
        userRepository.save(target);
    }
}
