package com.ranafahad.unifriends.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UnreadCountRepository unreadCountRepository;

    public List<UnreadCount> getUnreadCounts(Long userId) {
        return unreadCountRepository.findByIdUserId(userId);
    }

    @Transactional
    public void initializeUnreadCount(Long userId, Long leagueId) {
        UnreadCountId id = new UnreadCountId(userId, leagueId);
        if (unreadCountRepository.findById(id).isEmpty()) {
            UnreadCount count = new UnreadCount(id, 0);
            unreadCountRepository.save(count);
        }
    }

    @Transactional
    public void incrementUnreadCounts(Long leagueId, Long senderUserId) {
        unreadCountRepository.incrementForLeagueExceptSender(leagueId, senderUserId);
    }

    @Transactional
    public void resetUnreadCount(Long userId, Long leagueId) {
        unreadCountRepository.findByIdUserIdAndIdLeagueId(userId, leagueId)
                .ifPresent(uc -> {
                    uc.setCount(0);
                    unreadCountRepository.save(uc);
                });
    }
}
