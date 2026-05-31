package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.entity.Match;
import com.padel.padelmanagement.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Matches")
@CrossOrigin(origins = "http://localhost:4200")
public class MatchController {

    @Autowired
    private MatchRepository matchRepository;

    // Récupérer tous les matchs pour les afficher sur Angular
    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchRepository.findAll());
    }

    // Créer une nouvelle réservation de padel
    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        // Le cahier des charges exige que le match dure 1h30 + 15 min.
        // On gèrera les contrôles de chevauchement de dates ici si on a le temps.
        Match savedMatch = matchRepository.save(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMatch);
    }
}