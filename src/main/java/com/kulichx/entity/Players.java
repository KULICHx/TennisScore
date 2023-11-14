package com.kulichx.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PLAYERS", schema = "PUBLIC", catalog = "TENNISSCORE")
public class Players {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @Basic
    @Column(name = "NAME")
    private String name;

}
