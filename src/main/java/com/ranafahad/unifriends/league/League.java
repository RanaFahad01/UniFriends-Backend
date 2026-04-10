package com.ranafahad.unifriends.league;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leagues")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeagueType type;

    private String description;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @Builder.Default
    private List<LeagueMember> members = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
}
