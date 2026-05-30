package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.service.MembreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Membre")
@CrossOrigin(origins = "http://localhost:4200")
public class MembreController {

    @Autowired
    private MembreService membreService;

    // 1. UNIQUE ROUTE GET : Récupérer tous les membres
    @GetMapping
    public ResponseEntity<List<Membre>> getAllMembre() {
        return ResponseEntity.ok(membreService.getAllMembre());
    }

    // 2. UNIQUE ROUTE POST : Ajouter un membre avec simulation de Token
    @PostMapping
    public ResponseEntity<?> addMembre(
            @RequestBody Membre membre,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // --- SIMULATION SÉCURITÉ JWT ---
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erreur : Authentification requise. Token JWT manquant.");
        }

        String extractedToken = token.substring(7);

        if (!"mock-pdw-token-admin".equals(extractedToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erreur : Accès refusé. Rôle 'ADMIN' nécessaire.");
        }
        // -------------------------------

        Membre createdMembre = membreService.createMembre(membre);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMembre);
    }
}