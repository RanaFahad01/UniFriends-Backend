package com.ranafahad.unifriends.league;

import com.ranafahad.unifriends.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "league_members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeagueMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private League league;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private LeagueMemberRole memberRole;

    @CreationTimestamp
    private LocalDateTime joinedAt;
}
