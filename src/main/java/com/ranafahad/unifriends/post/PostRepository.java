package com.ranafahad.unifriends.post;

import com.ranafahad.unifriends.league.LeagueType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTypeOrderByCreatedAtDesc(LeagueType type, Pageable pageable);
}
