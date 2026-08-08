package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.service.MembreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.padel.padelmanagement.repository.ParticipationRepository;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.padel.padelmanagement.entity.Participation;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/Membres")

public class MembreController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MembreService membreService;

    @GetMapping
    public ResponseEntity<List<Membre>> getAllMembre() {
        return ResponseEntity.ok(membreService.getAllMembre());
    }

    @PostMapping
    public ResponseEntity<Membre> addMembre(@RequestBody Membre membre) {
        return registerPublic(membre);
    }

    @PostMapping("/register")
    public ResponseEntity<Membre> registerPublic(@RequestBody Membre membre) {
        if (membre.getMatricule() == null || membre.getMatricule().isEmpty()) {
            int randomNum = (int)(Math.random() * 9000) + 1000;
            membre.setMatricule("M" + randomNum);
        }

        membre.setSousPenalite(0);
        if (membre.getTypeMembre() == null) {
            membre.setTypeMembre("Standard");
        }

        membre.setRole("ROLE_USER");

        if (membre.getMotDePasse() != null && !membre.getMotDePasse().isEmpty()) {
            String motDePasseCrypte = passwordEncoder.encode(membre.getMotDePasse());
            membre.setMotDePasse(motDePasseCrypte);
        }

        Membre createdMembre = membreService.createMembre(membre);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMembre);
    }

    @PutMapping("/{matricule}")
    public ResponseEntity<Membre> updateMembre(@PathVariable String matricule, @RequestBody Membre details) {
        var membreOpt = membreService.getAllMembre().stream()
                .filter(m -> m.getMatricule().equals(matricule))
                .findFirst();

        if (membreOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Membre membreAUpdate = membreOpt.get();
        membreAUpdate.setNom(details.getNom());
        membreAUpdate.setPrenom(details.getPrenom());
        membreAUpdate.setEmail(details.getEmail());
        membreAUpdate.setRole(details.getRole());
        membreAUpdate.setTypeMembre(details.getTypeMembre());
        membreAUpdate.setSousPenalite(details.getSousPenalite());

        Membre saved = membreService.createMembre(membreAUpdate);
        return ResponseEntity.ok(saved);
    }

    @Autowired
    private ParticipationRepository participationRepository;

    @GetMapping("/{matricule}/historique")
    public ResponseEntity<?> getHistoriqueJoueur(@PathVariable String matricule) {
        try {
            System.out.println("🔍 [API] RECHERCHE HISTORIQUE POUR : " + matricule);

            List<Participation> participations = participationRepository.findByMembre_Matricule(matricule);
            System.out.println("👉 [API] PARTICIPATIONS TROUVÉES : " + participations.size());

            List<Map<String, Object>> historique = new ArrayList<>();

            for (Participation p : participations) {
                Map<String, Object> info = new HashMap<>();

                if (p.getMatch() == null) continue;

                info.put("idMatch", p.getMatch().getIdMatch());
                info.put("date", p.getMatch().getDateMatch());
                info.put("heure", p.getMatch().getHeureDebut());
                info.put("estOrganisateur", p.getEstOrganisateur());
                info.put("aPaye", p.getAPaye());

                String nomTerrain = "Terrain inconnu";
                if (p.getMatch().getTerrain() != null) {
                    nomTerrain = p.getMatch().getTerrain().getNomTerrain();
                }
                info.put("terrain", nomTerrain);

                List<String> autresJoueurs = new ArrayList<>();
                try {
                    List<Participation> coJoueursPart = participationRepository.findByMatch_IdMatch(p.getMatch().getIdMatch());
                    for (Participation part : coJoueursPart) {
                        if (part.getMembre() != null && !part.getMembre().getMatricule().equals(matricule)) {
                            autresJoueurs.add(part.getMembre().getPrenom() + " " + part.getMembre().getNom());
                        }
                    }
                } catch (Exception err) {
                    System.out.println("⚠️ Impossible de charger les co-joueurs : " + err.getMessage());
                }

                info.put("coJoueurs", autresJoueurs.isEmpty() ? "Aucun co-joueur inscrit" : String.join(", ", autresJoueurs));

                historique.add(info);
            }

            System.out.println("✅ [API] HISTORIQUE GÉNÉRÉ AVEC SUCCÈS !");
            return ResponseEntity.ok(historique);

        } catch (Exception e) {
            System.out.println("🔥 CRASH DANS L'HISTORIQUE :");
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur lors de la récupération de l'historique : " + e.getMessage());
        }
    }
}