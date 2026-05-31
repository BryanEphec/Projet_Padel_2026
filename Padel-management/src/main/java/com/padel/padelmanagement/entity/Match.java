package com.padel.padelmanagement.entity;



import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
@Table(name = "[Match]") // Les crochets sont VITALES ici pour SQL Server
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMatch")
    private Long idMatch;

    @Column(name = "DateMatch")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateMatch;

    @Column(name = "HeureDebut")
    @JsonFormat(pattern = "HH:mm:ss")
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
    @Column(name = "DateHeure")
    private java.time.LocalDateTime dateHeure;

    public java.time.LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(java.time.LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
}