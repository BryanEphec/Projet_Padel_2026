package com.padel.padelmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "[Match]") // Les crochets sont VITALES ici pour SQL Server
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMatch")
    private Long idMatch;

    @Column(name = "DateMatch")
    private LocalDate dateMatch;

    @Column(name = "HeureDebut")
    private LocalTime heureDebut;

    @Column(name = "EstPrive")
    private Boolean estPrive;

    @ManyToOne
    @JoinColumn(name = "IdTerrain")
    private Terrain terrain;

    // --- GETTERS & SETTERS ---
    public Long getIdMatch() { return idMatch; }
    public void setIdMatch(Long idMatch) { this.idMatch = idMatch; }

    public LocalDate getDateMatch() { return dateMatch; }
    public void setDateMatch(LocalDate dateMatch) { this.dateMatch = dateMatch; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public Boolean getEstPrive() { return estPrive; }
    public void setEstPrive(Boolean estPrive) { this.estPrive = estPrive; }

    public Terrain getTerrain() { return terrain; }
    public void setTerrain(Terrain terrain) { this.terrain = terrain; }
}