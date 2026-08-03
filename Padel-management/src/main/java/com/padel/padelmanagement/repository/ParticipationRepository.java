package com.padel.padelmanagement.repository;

import com.padel.padelmanagement.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Participation.ParticipationId> {

    // Trouve toutes les participations d'un joueur
    List<Participation> findByMembre_Matricule(String matricule);

    // Trouve toutes les participations d'un match (pour trouver les co-joueurs)
    List<Participation> findByMatch_IdMatch(Long idMatch);
}