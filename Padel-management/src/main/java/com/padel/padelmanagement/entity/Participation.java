package com.padel.padelmanagement.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Participation")
@IdClass(Participation.ParticipationId.class) // Gestion de la clé composée obligatoire pour JPA
public class Participation {

    @Id
    @ManyToOne
    @JoinColumn(name = "Matricule")
    private Membre membre;

    @Id
    @ManyToOne
    @JoinColumn(name = "IdMatch")
    private Match match;

    @Column(name = "EstOrganisateur")
    private Boolean estOrganisateur;

    @Column(name = "APaye")
    private Boolean aPaye;

    // --- CLASSE INTERNE POUR LA CLÉ COMPOSÉE ---
    public static class ParticipationId implements Serializable {
        private String membre;
        private Long match;

        public ParticipationId() {}
        public ParticipationId(String membre, Long match) {
            this.membre = membre;
            this.match = match;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParticipationId that = (ParticipationId) o;
            return Objects.equals(membre, that.membre) && Objects.equals(match, that.match);
        }
        @Override
        public int hashCode() {
            return Objects.hash(membre, match);
        }
    }

    // --- GETTERS & SETTERS ---
    public Membre getMembre() { return membre; }
    public void setMembre(Membre membre) { this.membre = membre; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public Boolean getEstOrganisateur() { return estOrganisateur; }
    public void setEstOrganisateur(Boolean estOrganisateur) { this.estOrganisateur = estOrganisateur; }

    public Boolean getAPaye() { return aPaye; }
    public void setAPaye(Boolean aPaye) { this.aPaye = aPaye; }
}