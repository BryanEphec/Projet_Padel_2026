package com.padel.padelmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MatchRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateMatch;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime heureDebut;

    private Boolean estPrive;
    private Long idTerrain;
    private String matriculeOrganisateur;
    private List<String> autresJoueursMatricules;

    public LocalDate getDateMatch() { return dateMatch; }
    public void setDateMatch(LocalDate dateMatch) { this.dateMatch = dateMatch; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public Boolean getEstPrive() { return estPrive; }
    public void setEstPrive(Boolean estPrive) { this.estPrive = estPrive; }

    public Long getIdTerrain() { return idTerrain; }
    public void setIdTerrain(Long idTerrain) { this.idTerrain = idTerrain; }

    public String getMatriculeOrganisateur() { return matriculeOrganisateur; }
    public void setMatriculeOrganisateur(String matriculeOrganisateur) { this.matriculeOrganisateur = matriculeOrganisateur; }

    public List<String> getAutresJoueursMatricules() { return autresJoueursMatricules; }
    public void setAutresJoueursMatricules(List<String> autresJoueursMatricules) { this.autresJoueursMatricules = autresJoueursMatricules; }
}