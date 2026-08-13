package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.dto.MatchRequest;
import com.padel.padelmanagement.entity.Match;
import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.entity.Terrain;
import com.padel.padelmanagement.repository.MatchRepository;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.repository.ParticipationRepository;
import com.padel.padelmanagement.repository.TerrainRepository;
import com.padel.padelmanagement.service.ReservationService;
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

    @Mock private MatchRepository matchRepository;
    @Mock private ParticipationRepository participationRepository;
    @Mock private MembreRepository membreRepository;
    @Mock private TerrainRepository terrainRepository;
    @Mock private ReservationService reservationService;

    @InjectMocks
    private MatchController matchController;

    @Test
    void testCreateMatch_Success() {
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

        when(membreRepository.findById("M7777")).thenReturn(Optional.of(mockMembre));
        when(terrainRepository.findById(1L)).thenReturn(Optional.of(mockTerrain));
        when(matchRepository.save(any(Match.class))).thenReturn(mockMatchSauvegarde);
        doNothing().when(reservationService).verifierDroitsSelonMatricule(anyString(), any(LocalDate.class));

        ResponseEntity<?> response = matchController.createMatch(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(matchRepository, times(1)).save(any(Match.class));
        verify(participationRepository, times(1)).save(any());
    }
}