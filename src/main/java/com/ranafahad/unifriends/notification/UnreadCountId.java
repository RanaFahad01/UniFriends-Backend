package com.ranafahad.unifriends.notification;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnreadCountId implements Serializable {

    private Long userId;

    private Long leagueId;
}
