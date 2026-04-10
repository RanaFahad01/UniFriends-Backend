package com.ranafahad.unifriends.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unread_counts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnreadCount {

    @EmbeddedId
    private UnreadCountId id;

    @Column(nullable = false)
    private int count = 0;
}
