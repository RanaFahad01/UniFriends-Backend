package com.ranafahad.unifriends.league;

import com.ranafahad.unifriends.notification.NotificationService;
import com.ranafahad.unifriends.user.User;
import com.ranafahad.unifriends.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final LeagueMemberRepository leagueMemberRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public List<League> findByType(LeagueType type) {
        return leagueRepository.findByType(type);
    }

    public List<League> findLeaguesByUserEmail(String email) {
        User user = userService.findByEmail(email);
        return leagueMemberRepository.findByUserId(user.getId()).stream()
                .map(LeagueMember::getLeague)
                .toList();
    }

    public League findById(Long id) {
        return leagueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("League not found"));
    }

    @Transactional
    public League createLeague(String callerEmail, String name, LeagueType type, String description, String mascot) {
        User user = userService.findByEmail(callerEmail);

        if (user.getBannedAt() != null) {
            throw new IllegalStateException("Banned users cannot create leagues");
        }

        // Check user isn't already in a league of this type
        checkNotInLeagueOfType(user, type);

        League league = League.builder()
                .name(name)
                .type(type)
                .description(description)
                .mascot(mascot)
                .build();
        league = leagueRepository.save(league);

        // Creator becomes SENIOR or BIG_HOMIE
        LeagueMemberRole role = type == LeagueType.ACADEMIC ? LeagueMemberRole.SENIOR : LeagueMemberRole.BIG_HOMIE;
        LeagueMember member = LeagueMember.builder()
                .league(league)
                .user(user)
                .memberRole(role)
                .build();
        leagueMemberRepository.save(member);

        // Initialize unread count for creator
        notificationService.initializeUnreadCount(user.getId(), league.getId());

        return league;
    }

    @Transactional
    public void joinLeague(Long leagueId, String callerEmail) {
        User user = userService.findByEmail(callerEmail);
        League league = findById(leagueId);

        // Rule 1: not banned
        if (user.getBannedAt() != null) {
            throw new IllegalStateException("Banned users cannot join leagues");
        }
        // Rule 2: not already in a league of this type
        checkNotInLeagueOfType(user, league.getType());
        // Rule 3: league under 30 members
        if (leagueMemberRepository.countByLeagueId(leagueId) >= 30) {
            throw new IllegalStateException("This league is full (max 30 members)");
        }

        LeagueMember member = LeagueMember.builder()
                .league(league)
                .user(user)
                .memberRole(LeagueMemberRole.MEMBER)
                .build();
        leagueMemberRepository.save(member);

        notificationService.initializeUnreadCount(user.getId(), league.getId());
    }

    @Transactional
    public void removeMember(Long leagueId, Long userId, String callerEmail) {
        User caller = userService.findByEmail(callerEmail);
        LeagueMember callerMember = leagueMemberRepository.findByLeagueIdAndUserId(leagueId, caller.getId())
                .orElseThrow(() -> new AccessDeniedException("You are not a member of this league"));

        // Only SENIOR or BIG_HOMIE can remove members
        if (callerMember.getMemberRole() == LeagueMemberRole.MEMBER) {
            throw new AccessDeniedException("Only league leaders can remove members");
        }

        LeagueMember target = leagueMemberRepository.findByLeagueIdAndUserId(leagueId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found in this league"));
        leagueMemberRepository.delete(target);
    }

    private void checkNotInLeagueOfType(User user, LeagueType type) {
        List<LeagueMember> memberships = leagueMemberRepository.findByUserId(user.getId());
        boolean alreadyInType = memberships.stream()
                .anyMatch(m -> m.getLeague().getType() == type);
        if (alreadyInType) {
            throw new IllegalStateException(
                    "You are already in a" + (type == LeagueType.ACADEMIC ? "n academic" : "n extracurriculars") + " league");
        }
    }
}
