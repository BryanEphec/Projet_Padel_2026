package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.dto.MatchRequest;
import com.padel.padelmanagement.entity.Match;
import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.entity.Terrain;
import com.padel.padelmanagement.repository.MatchRepository;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.repository.ParticipationRepository;
import com.padel.padelmanagement.repository.TerrainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchControllerTest {

    // On "Mock" (simule) les repositories pour ne pas toucher à la vraie base de données MSSQL
    @Mock private MatchRepository matchRepository;
    @Mock private ParticipationRepository participationRepository;
    @Mock private MembreRepository membreRepository;
    @Mock private TerrainRepository terrainRepository;

    // On injecte nos simulations dans le vrai MatchController
    @InjectMocks
    private MatchController matchController;

    @Test
    void testCreateMatch_Success() {
        // --------------------------------------------------------
        // 1. GIVEN : On prépare le contexte (Les données d'entrée)
        // --------------------------------------------------------
        MatchRequest request = new MatchRequest();
        request.setMatriculeOrganisateur("M7777");
        request.setIdTerrain(1L);
        request.setDateMatch(LocalDate.of(2026, 6, 15));
        request.setHeureDebut(LocalTime.of(18, 0));
        request.setEstPrive(false);

        Membre mockMembre = new Membre();
        mockMembre.setMatricule("M7777");

        Terrain mockTerrain = new Terrain();
        mockTerrain.setIdTerrain(1L);

        Match mockMatchSauvegarde = new Match();
        mockMatchSauvegarde.setIdMatch(100L);

        // On dicte aux faux repositories comment ils doivent réagir
        when(membreRepository.findById("M7777")).thenReturn(Optional.of(mockMembre));
        when(terrainRepository.findById(1L)).thenReturn(Optional.of(mockTerrain));
        when(matchRepository.save(any(Match.class))).thenReturn(mockMatchSauvegarde);

        // --------------------------------------------------------
        // 2. WHEN : On exécute l'action (L'appel du contrôleur)
        // --------------------------------------------------------
        ResponseEntity<?> response = matchController.createMatch(request);

        // --------------------------------------------------------
        // 3. THEN : On vérifie que le résultat est parfait
        // --------------------------------------------------------

        // A. Est-ce que le serveur a bien répondu "201 CREATED" ?
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // B. Est-ce que la réponse n'est pas vide ?
        assertNotNull(response.getBody());

        // C. Est-ce que la base de données a bien été appelée exactement 1 fois pour sauver le match ?
        verify(matchRepository, times(1)).save(any(Match.class));

        // D. Est-ce que le joueur a bien été inscrit (sauvegarde dans la table Participation) ?
        verify(participationRepository, times(1)).save(any());
    }
}