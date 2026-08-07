package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.dto.MatchRequest;
import com.padel.padelmanagement.entity.Match;
import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.entity.Participation;
import com.padel.padelmanagement.repository.MatchRepository;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.repository.ParticipationRepository;
import com.padel.padelmanagement.repository.TerrainRepository;
import com.padel.padelmanagement.service.ReservationService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/Matches")
@CrossOrigin(origins = "http://localhost:4200")
public class MatchController {

    @Autowired private MatchRepository matchRepository;
    @Autowired private ParticipationRepository participationRepository;
    @Autowired private MembreRepository membreRepository;
    @Autowired private TerrainRepository terrainRepository;
    @Autowired private ReservationService reservationService;
    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createMatch(@Valid @RequestBody MatchRequest request) {
       reservationService.verifierDroitsSelonMatricule(request.getMatriculeOrganisateur(),request.getDateMatch());

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
    // ROUTE GET : Récupérer les matchs publics non complets
    @GetMapping("/publics")
    public ResponseEntity<List<java.util.Map<String, Object>>> getPublicMatches() {
        List<Match> matchs = matchRepository.findAll().stream()
                .filter(m -> Boolean.FALSE.equals(m.getEstPrive()))
                .toList();

        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();

        for (Match m : matchs) {
            List<Participation> parts = participationRepository.findByMatch_IdMatch(m.getIdMatch());

            if (parts.size() < 4) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("idMatch", m.getIdMatch());
                map.put("date", m.getDateMatch());
                map.put("heure", m.getHeureDebut());
                map.put("terrain", m.getTerrain() != null ? m.getTerrain().getNomTerrain() : "Inconnu");
                map.put("nbJoueurs", parts.size());

                // 🌟 AJOUT : Trouver l'organisateur (celui qui a estOrganisateur = true)
                String organisateurNom = parts.stream()
                        .filter(p -> Boolean.TRUE.equals(p.getEstOrganisateur()))
                        .map(p -> p.getMembre().getPrenom() + " " + p.getMembre().getNom())
                        .findFirst()
                        .orElse("Inconnu");
                map.put("organisateur", organisateurNom);

                result.add(map);
            }
        }
        return ResponseEntity.ok(result);
    }

    public static class JoinRequest {
        @NotBlank(message = "Le matricule est obligatoire pour rejoindre un match")
        private String matricule;
        public String getMatricule() { return matricule; }
        public void setMatricule(String matricule) { this.matricule = matricule; }
    }

    // ROUTE POST : Rejoindre un match public
    @PostMapping("/{idMatch}/join")
    public ResponseEntity<?> rejoindreMatch(@PathVariable Long idMatch,@Valid @RequestBody JoinRequest payload) {
        try {
            System.out.println("👉 TENTATIVE DE REJOINDRE LE MATCH " + idMatch + " PAR : " + payload.getMatricule());
            String matricule = payload.getMatricule();

            Match match = matchRepository.findById(idMatch).orElse(null);
            if (match == null) return ResponseEntity.badRequest().body("Match introuvable");

            // Sécurité anti-null : on utilise Boolean.TRUE.equals() au cas où la base de données renvoie null
            if (Boolean.TRUE.equals(match.getEstPrive())) {
                return ResponseEntity.badRequest().body("Ce match est privé");
            }

            Membre membre = membreRepository.findById(matricule).orElse(null);
            if (membre == null) return ResponseEntity.badRequest().body("Membre introuvable");

            List<Participation> participants = participationRepository.findByMatch_IdMatch(idMatch);

            // RÈGLE 1 : Maximum 4 joueurs
            if (participants.size() >= 4) {
                System.out.println("❌ ÉCHEC : Match complet");
                return ResponseEntity.badRequest().body("Ce match est déjà complet (4 joueurs max)");
            }

            // RÈGLE 2 : Ne pas s'inscrire deux fois
            boolean alreadyJoined = participants.stream().anyMatch(p -> p.getMembre().getMatricule().equals(matricule));
            if (alreadyJoined) {
                System.out.println("❌ ÉCHEC : Joueur déjà inscrit");
                return ResponseEntity.badRequest().body("Vous participez déjà à ce match");
            }

            // TOUT EST BON !
            Participation p = new Participation();
            p.setMatch(match);
            p.setMembre(membre);
            p.setEstOrganisateur(false);
            p.setAPaye(false);
            participationRepository.save(p);

            System.out.println("✅ SUCCÈS : " + membre.getPrenom() + " a rejoint le match !");
            return ResponseEntity.ok("Vous avez rejoint le match avec succès !");

        } catch (Exception e) {
            System.out.println("🔥 CRASH LORS DE L'INSCRIPTION AU MATCH :");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur lors de l'inscription : " + e.getMessage());
        }
    }
}