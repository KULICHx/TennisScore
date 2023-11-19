package com.kulichx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MATCHES", schema = "PUBLIC", catalog = "TENNISSCORE")
public class Matches {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @ManyToOne
    @JoinColumn(name = "PLAYER1")
    private Players player1;
    @ManyToOne
    @JoinColumn(name = "PLAYER2")
    private Players player2;
    @ManyToOne
    @JoinColumn(name = "WINNER")
    private Players winner;

    public Matches() {
    }
    public Matches(Players player1, Players player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

}
