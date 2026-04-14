package com.padel.padelmanagement.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Sites")
@Data
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    private String adresse;
}
