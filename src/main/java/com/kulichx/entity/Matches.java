package com.kulichx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "matches", schema = "TennisScore")
public class Matches {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "player1")
    private Players player1;
    @ManyToOne
    @JoinColumn(name = "player2")
    private Players player2;
    @ManyToOne
    @JoinColumn(name = "winner")
    private Players winner;

    @Embedded
    private MatchScore matchScore;
    public Matches(Players player1, Players player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Matches() {

    }

}
