package com.padel.padelmanagement.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class MatchRequest {

    @NotBlank(message = "Le matricule de l'organisateur est obligatoire")
    private String matriculeOrganisateur;

    @NotNull(message = "Veuillez sélectionner un terrain")
    private Long idTerrain;

    @NotNull(message = "La date du match est obligatoire")
    @FutureOrPresent(message = "Impossible de réserver un terrain dans le passé !") // 🛡️ La magie est ici !
    private LocalDate dateMatch;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @NotNull
    private Boolean estPrive;
    private Boolean aPaye;

    public Boolean getAPaye() { return aPaye; }
    public void setAPaye(Boolean aPaye) { this.aPaye = aPaye; }

    // --- GETTERS & SETTERS ---
    public String getMatriculeOrganisateur() { return matriculeOrganisateur; }
    public void setMatriculeOrganisateur(String matriculeOrganisateur) { this.matriculeOrganisateur = matriculeOrganisateur; }

    public Long getIdTerrain() { return idTerrain; }
    public void setIdTerrain(Long idTerrain) { this.idTerrain = idTerrain; }

    public LocalDate getDateMatch() { return dateMatch; }
    public void setDateMatch(LocalDate dateMatch) { this.dateMatch = dateMatch; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public Boolean getEstPrive() { return estPrive; }
    public void setEstPrive(Boolean estPrive) { this.estPrive = estPrive; }
}