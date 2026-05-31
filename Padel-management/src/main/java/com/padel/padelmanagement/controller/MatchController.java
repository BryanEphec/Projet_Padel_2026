package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.dto.MatchRequest;
import com.padel.padelmanagement.entity.Match;
import com.padel.padelmanagement.entity.Participation;
import com.padel.padelmanagement.repository.MatchRepository;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.repository.ParticipationRepository;
import com.padel.padelmanagement.repository.TerrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Matches")
@CrossOrigin(origins = "http://localhost:4200")
public class MatchController {

    @Autowired private MatchRepository matchRepository;
    @Autowired private ParticipationRepository participationRepository;
    @Autowired private MembreRepository membreRepository;
    @Autowired private TerrainRepository terrainRepository;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createMatch(@RequestBody MatchRequest request) {
        try {
            System.out.println("👉 TENTATIVE DE RÉSERVATION : Date=" + request.getDateMatch() + ", Heure=" + request.getHeureDebut());

            var membreOpt = membreRepository.findById(request.getMatriculeOrganisateur());
            if (membreOpt.isEmpty()) {
                System.out.println("❌ ERREUR : Membre introuvable !");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Membre introuvable");
            }

            var terrainOpt = terrainRepository.findById(request.getIdTerrain());
            if (terrainOpt.isEmpty()) {
                System.out.println("❌ ERREUR : Terrain introuvable !");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Terrain introuvable");
            }

            Match match = new Match();
            match.setDateMatch(request.getDateMatch());
            match.setHeureDebut(request.getHeureDebut());
            match.setEstPrive(request.getEstPrive());
            match.setTerrain(terrainOpt.get());

            // 🔥 LA CORRECTION EST ICI : On crée la DateHeure combinée pour SQL Server
            if (request.getDateMatch() != null && request.getHeureDebut() != null) {
                match.setDateHeure(java.time.LocalDateTime.of(request.getDateMatch(), request.getHeureDebut()));
            }

            Match savedMatch = matchRepository.save(match);

            Participation org = new Participation();
            org.setMatch(savedMatch);
            org.setMembre(membreOpt.get());
            org.setEstOrganisateur(true);
            org.setAPaye(false);
            participationRepository.save(org);

            System.out.println("✅ MATCH CRÉÉ AVEC SUCCÈS !");
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMatch);

        } catch (Exception e) {
            System.out.println("🔥 CRASH DU SERVEUR LORS DE LA RÉSERVATION :");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }
}