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

import java.util.List;

@RestController
@RequestMapping("/api/Membres")
@CrossOrigin(origins = "http://localhost:4200")
public class MembreController {

    @Autowired
    private MembreService membreService;

    // 1. ROUTE GET : Récupérer tous les membres
    @GetMapping
    public ResponseEntity<List<Membre>> getAllMembre() {
        return ResponseEntity.ok(membreService.getAllMembre());
    }

    // 2. ROUTE POST STANDARD : Pour la compatibilité si ton Angular appelle simplement /api/Membres
    @PostMapping
    public ResponseEntity<Membre> addMembre(@RequestBody Membre membre) {
        return registerPublic(membre); // Redirige vers la logique de création ci-dessous
    }

    // 3. ROUTE PUBLIQUE POST : Inscription d'un nouveau joueur
    @PostMapping("/register")
    public ResponseEntity<Membre> registerPublic(@RequestBody Membre membre) {

        // Générer un matricule aléatoire si vide (ex: M4512)
        if (membre.getMatricule() == null || membre.getMatricule().isEmpty()) {
            int randomNum = (int)(Math.random() * 9000) + 1000;
            membre.setMatricule("M" + randomNum);
        }

        // Valeurs par défaut pour un nouveau joueur
        membre.setSousPenalite(0);
        if (membre.getTypeMembre() == null) {
            membre.setTypeMembre("Standard");
        }

        // 🔥 NOUVEAU : On assigne d'office le rôle "Joueur" pour la sécurité
        membre.setRole("ROLE_USER");

        Membre createdMembre = membreService.createMembre(membre);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMembre);
    }
    // 4. ROUTE PUT : Mettre à jour un membre existant (Réservé à l'admin normalement)
    @PutMapping("/{matricule}")
    public ResponseEntity<Membre> updateMembre(@PathVariable String matricule, @RequestBody Membre details) {
        // On cherche le membre existant
        var membreOpt = membreService.getAllMembre().stream()
                .filter(m -> m.getMatricule().equals(matricule))
                .findFirst();

        if (membreOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Membre membreAUpdate = membreOpt.get();
        // On met à jour les champs
        membreAUpdate.setNom(details.getNom());
        membreAUpdate.setPrenom(details.getPrenom());
        membreAUpdate.setEmail(details.getEmail());
        membreAUpdate.setRole(details.getRole());
        membreAUpdate.setTypeMembre(details.getTypeMembre());
        membreAUpdate.setSousPenalite(details.getSousPenalite());

        // On sauvegarde
        Membre saved = membreService.createMembre(membreAUpdate); // createMembre fait un "save" donc ça mettra à jour
        return ResponseEntity.ok(saved);
    }
    @Autowired
    private ParticipationRepository participationRepository;

    // 5. ROUTE GET : Historique détaillé d'un membre
    @GetMapping("/{matricule}/historique")
    public ResponseEntity<?> getHistoriqueJoueur(@PathVariable String matricule) {
        try {
            System.out.println("🔍 [API] RECHERCHE HISTORIQUE POUR : " + matricule);

            List<Participation> participations = participationRepository.findByMembre_Matricule(matricule);
            System.out.println("👉 [API] PARTICIPATIONS TROUVÉES : " + participations.size());

            List<Map<String, Object>> historique = new ArrayList<>();

            for (Participation p : participations) {
                Map<String, Object> info = new HashMap<>();

                // Sécurité absolue : si le match a été mal enregistré, on l'ignore pour ne pas faire planter la boucle
                if (p.getMatch() == null) continue;

                info.put("idMatch", p.getMatch().getIdMatch());
                info.put("date", p.getMatch().getDateMatch());
                info.put("heure", p.getMatch().getHeureDebut());
                info.put("estOrganisateur", p.getEstOrganisateur());
                info.put("aPaye", p.getAPaye());

                // Sécurité sur le terrain
                String nomTerrain = "Terrain inconnu";
                if (p.getMatch().getTerrain() != null) {
                    nomTerrain = p.getMatch().getTerrain().getNomTerrain();
                }
                info.put("terrain", nomTerrain);

                // Recherche des co-joueurs sous protection
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
            e.printStackTrace(); // Affiche la ligne exacte de l'erreur dans IntelliJ

            // En renvoyant une réponse "propre" même pour une erreur, Angular ne fera plus d'erreur CORS
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur lors de la récupération de l'historique : " + e.getMessage());
        }
    }
}