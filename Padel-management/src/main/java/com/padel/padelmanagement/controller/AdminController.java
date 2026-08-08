package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.repository.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    private final MembreRepository membreRepository;
    private final ReservationRepository reservationRepository;

    public AdminController(MembreRepository membreRepository, ReservationRepository reservationRepository) {
        this.membreRepository = membreRepository;
        this.reservationRepository = reservationRepository;
    }

    // 1. Pour la case "Membres Actifs"
    @GetMapping("/stats/membres-count")
    public ResponseEntity<Map<String, Long>> getMembresCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("totalMembres", membreRepository.count());
        return ResponseEntity.ok(response);
    }

    // 2. Pour la case "Total Réservations"
    @GetMapping("/stats/reservations-count")
    public ResponseEntity<Map<String, Long>> getReservationsCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("totalReservations", reservationRepository.count());
        return ResponseEntity.ok(response);
    }

    // 3. Pour la case "Chiffre d'Affaires"
    @GetMapping("/stats/chiffre-affaires")
    public ResponseEntity<Map<String, Double>> getChiffreAffaires() {
        long totalResa = reservationRepository.count();
        double prixTerrain = 20.0; // Tu peux modifier ce prix moyen ici !

        Map<String, Double> response = new HashMap<>();
        response.put("chiffreAffaires", totalResa * prixTerrain);
        return ResponseEntity.ok(response);
    }

    // 4. Pour le grand tableau "Gestion des Membres"
    @GetMapping("/membres")
    public ResponseEntity<List<Membre>> getAllMembres() {
        return ResponseEntity.ok(membreRepository.findAll());
    }
}