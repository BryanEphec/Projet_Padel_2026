package com.padel.padelmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "Membre")
public class Membre {

    @Id
    @Column(name = "Matricule")
    private String matricule;

    @Column(name = "Nom")
    private String nom;

    @Column(name = "Prenom")
    private String prenom;

    @Column(name = "Type")
    private String typeMembre;

    @Column(name = "IdSiteRatt")
    private Long idSiteRattachement;

    @Column(name = "SousPenalite")
    private Integer sousPenalite;

    // --- GETTERS & SETTERS ---
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTypeMembre() { return typeMembre; }
    public void setTypeMembre(String typeMembre) { this.typeMembre = typeMembre; }

    public Long getIdSiteRattachement() { return idSiteRattachement; }
    public void setIdSiteRattachement(Long idSiteRattachement) { this.idSiteRattachement = idSiteRattachement; }

    public Integer getSousPenalite() { return sousPenalite; }
    public void setSousPenalite(Integer sousPenalite) { this.sousPenalite = sousPenalite; }
}