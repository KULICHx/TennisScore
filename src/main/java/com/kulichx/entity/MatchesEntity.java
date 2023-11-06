package com.kulichx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MATCHES", schema = "PUBLIC", catalog = "TENNISSCORE")
public class MatchesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @Basic
    @Column(name = "PLAYER1")
    private Integer player1;
    @Basic
    @Column(name = "PLAYER2")
    private Integer player2;
    @Basic
    @Column(name = "WINNER")
    private Integer winner;

}
