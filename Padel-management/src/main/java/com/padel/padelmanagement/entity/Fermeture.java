package com.padel.padelmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Fermeture")
public class Fermeture {

    @Id
    @Column(name = "IdFermeture")
    private Long idFermeture;

    @Column(name = "DateFermeture")
    private LocalDate dateFermeture;

    @ManyToOne
    @JoinColumn(name = "IdSite")
    private Site site;

    // --- GETTERS & SETTERS ---
    public Long getIdFermeture() { return idFermeture; }
    public void setIdFermeture(Long idFermeture) { this.idFermeture = idFermeture; }

    public LocalDate getDateFermeture() { return dateFermeture; }
    public void setDateFermeture(LocalDate dateFermeture) { this.dateFermeture = dateFermeture; }

    public Site getSite() { return site; }
    public void setSite(Site site) { this.site = site; }
}