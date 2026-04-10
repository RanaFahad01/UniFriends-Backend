package com.ranafahad.unifriends.league;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeagueMemberRepository extends JpaRepository<LeagueMember, Long> {
    List<LeagueMember> findByUserId(Long userId);
    Optional<LeagueMember> findByLeagueIdAndUserId(Long leagueId, Long userId);
    boolean existsByLeagueIdAndUserId(Long leagueId, Long userId);
    int countByLeagueId(Long leagueId);
}
