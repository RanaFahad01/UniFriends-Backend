package com.ranafahad.unifriends.chat;

import com.ranafahad.unifriends.league.League;
import com.ranafahad.unifriends.league.LeagueMemberRepository;
import com.ranafahad.unifriends.notification.NotificationService;
import com.ranafahad.unifriends.user.User;
import com.ranafahad.unifriends.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final LeagueMemberRepository leagueMemberRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public Page<ChatMessage> getMessages(Long leagueId, String callerEmail, Pageable pageable) {
        User user = userService.findByEmail(callerEmail);

        // Only league members can read messages
        if (!leagueMemberRepository.existsByLeagueIdAndUserId(leagueId, user.getId())) {
            throw new AccessDeniedException("Only league members can read messages");
        }
        return chatMessageRepository.findByLeagueIdOrderBySentAtDesc(leagueId, pageable);
    }

    @Transactional
    public ChatMessage sendMessage(Long leagueId, String callerEmail, String content) {
        User user = userService.findByEmail(callerEmail);

        if (user.getBannedAt() != null) {
            throw new IllegalStateException("Banned users cannot send messages");
        }
        if (!leagueMemberRepository.existsByLeagueIdAndUserId(leagueId, user.getId())) {
            throw new AccessDeniedException("Only league members can send messages");
        }

        ChatMessage message = ChatMessage.builder()
                .league(League.builder().id(leagueId).build())
                .sender(user)
                .content(content)
                .build();
        message = chatMessageRepository.save(message);

        // Increment unread counts for other members
        notificationService.incrementUnreadCounts(leagueId, user.getId());

        return message;
    }

    @Transactional
    public void markRead(Long leagueId, String callerEmail) {
        User user = userService.findByEmail(callerEmail);
        notificationService.resetUnreadCount(user.getId(), leagueId);
    }
}
