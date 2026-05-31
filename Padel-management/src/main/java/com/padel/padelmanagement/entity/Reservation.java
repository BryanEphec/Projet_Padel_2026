package com.padel.padelmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Reservation")
    private Long id;

    @Column(name = "Date_Match")
    private LocalDate dateMatch;

    @Column(name = "Heure_Debut")
    private LocalTime heureDebut;

    @Column(name = "Statut") // "Prive" ou "Public"
    private String statut;

    @Column(name = "Solde_Du") // Pour gérer le prix de 60 euros
    private Double soldeDu;

    @ManyToOne
    @JoinColumn(name = "ID_Terrain")
    private Terrain terrain;

    @ManyToOne
    @JoinColumn(name = "Matricule_Organisateur")
    private Membre organisateur;

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDateMatch() { return dateMatch; }
    public void setDateMatch(LocalDate dateMatch) { this.dateMatch = dateMatch; }
    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Double getSoldeDu() { return soldeDu; }
    public void setSoldeDu(Double soldeDu) { this.soldeDu = soldeDu; }
    public Terrain getTerrain() { return terrain; }
    public void setTerrain(Terrain terrain) { this.terrain = terrain; }
    public Membre getOrganisateur() { return organisateur; }
    public void setOrganisateur(Membre organisateur) { this.organisateur = organisateur; }
}