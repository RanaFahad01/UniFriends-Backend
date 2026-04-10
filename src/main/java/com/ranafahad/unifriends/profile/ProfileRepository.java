package com.ranafahad.unifriends.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserId(Long userId);
    Optional<Profile> findByUserIdAndType(Long userId, ProfileType type);
}
