package com.padel.padelmanagement.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Table(name = "Sites")
@Data
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Site")
    private Long id;

    @Column(name = "Nom_Site",nullable = false, length = 100)
    private String Nom_Site;

    @Column(name = "Heure_Ouverture")
    private LocalTime Heure_Ouverture;

    @Column(name = "Heure_Fermeture")
    private LocalTime Heure_Fermeture;
}
