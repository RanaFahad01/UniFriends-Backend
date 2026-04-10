package com.ranafahad.unifriends.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UnreadCountRepository extends JpaRepository<UnreadCount, UnreadCountId> {
    List<UnreadCount> findByIdUserId(Long userId);
    Optional<UnreadCount> findByIdUserIdAndIdLeagueId(Long userId, Long leagueId);

    @Modifying
    @Query("UPDATE UnreadCount u SET u.count = u.count + 1 WHERE u.id.leagueId = :leagueId AND u.id.userId <> :senderUserId")
    void incrementForLeagueExceptSender(Long leagueId, Long senderUserId);
}
