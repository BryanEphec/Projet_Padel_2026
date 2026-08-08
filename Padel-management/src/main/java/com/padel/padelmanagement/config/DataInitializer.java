package com.padel.padelmanagement.config;

import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.entity.Site;
import com.padel.padelmanagement.entity.Terrain;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.repository.SiteRepository;
import com.padel.padelmanagement.repository.TerrainRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MembreRepository membreRepository;
    private final SiteRepository siteRepository;
    private final TerrainRepository terrainRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MembreRepository membreRepository,
                           SiteRepository siteRepository,
                           TerrainRepository terrainRepository,
                           PasswordEncoder passwordEncoder) {
        this.membreRepository = membreRepository;
        this.siteRepository = siteRepository;
        this.terrainRepository = terrainRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("--- Vérification des données de la base ---");

        // 1. On crée un Site et des Terrains uniquement si la table est vide
        if (siteRepository.count() == 0) {
            Site site = new Site();
            site.setNomSite("Padel Brussels Central");
            site.setHeureOuverture(LocalTime.of(8, 0));
            site.setHeureFermeture(LocalTime.of(23, 0));
            site = siteRepository.save(site);

            Terrain t1 = new Terrain();
            t1.setNomTerrain("Indoor 1 - Atomium (Standard)");
            t1.setIdSite(site.getId());
            terrainRepository.save(t1);

            Terrain t2 = new Terrain();
            t2.setNomTerrain("Indoor 2 - Sablon (Standard)");
            t2.setIdSite(site.getId());
            terrainRepository.save(t2);

            Terrain t3 = new Terrain();
            t3.setNomTerrain("Outdoor - Uccle Panoramique");
            t3.setIdSite(site.getId());
            terrainRepository.save(t3);

            Terrain t4 = new Terrain();
            t4.setNomTerrain("Terrain VIP - Cinquantenaire");
            t4.setIdSite(site.getId());
            terrainRepository.save(t4);

            System.out.println("👉 Site et Terrains (Atomium, Sablon, Uccle, Cinquantenaire) insérés avec succès !");
        }

        // 2. On insère le membre Thomas Lefebvre (ROLE_ADMIN)
        if (!membreRepository.existsById("M7778")) {
            Membre adminMembre = new Membre();
            adminMembre.setMatricule("M7778");
            adminMembre.setNom("Lefebvre");
            adminMembre.setPrenom("Thomas");
            adminMembre.setTypeMembre("Standard");
            adminMembre.setEmail("admin@padel.be");
            adminMembre.setMotDePasse(passwordEncoder.encode("admin123"));
            adminMembre.setRole("ROLE_ADMIN");

            try {
                membreRepository.save(adminMembre);
                System.out.println("👉 Admin de test ('Thomas Lefebvre') inséré avec succès !");
            } catch (Exception e) {
                System.out.println("⚠️ Note : L'insertion a échoué pour M7778.");
            }
        }

        // 3. On insère le profil principal (ROLE_USER)
        if (!membreRepository.existsById("C61CCAB")) {
            Membre userMembre = new Membre();
            userMembre.setMatricule("C61CCAB");
            userMembre.setNom("Galvao Coutinho");
            userMembre.setPrenom("Bryan");
            userMembre.setTypeMembre("Junior");
            userMembre.setEmail("bryan@padel.be");
            userMembre.setMotDePasse(passwordEncoder.encode("password"));
            userMembre.setRole("ROLE_USER");

            try {
                membreRepository.save(userMembre);
                System.out.println("👉 Joueur Bryan inséré avec succès !");
            } catch (Exception e) {
                System.out.println("⚠️ Note : L'insertion a échoué pour C61CCAB.");
            }
        }

        System.out.println("--- Initialisation terminée ---");
    }
}