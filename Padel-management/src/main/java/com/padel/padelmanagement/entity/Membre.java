package com.padel.padelmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Membres")
@Data
public class Membre {

    @Id
    @Column(name = "Matricule")
    private Long matricule;

    @Column(name = "Nom", nullable = false)
    private String nom;

    @Column(name = "Prenom", nullable = false)
    private String prenom;

    @Column(name = "Type_Membre")
    private String typeMembre;

    @Column(name = "ID_Site_Rattachement")
    private Long idSite;
}
