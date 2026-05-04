package com.padel.padelmanagement.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Terrains")
@Data
public class Terrain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Terrain")
    private Long idTerrain;

    @Column(name = "Nom_Terrain", nullable = false)
    private String nomTerrain;

    @Column(name = "ID_Site")
    private Long idSite;
}
