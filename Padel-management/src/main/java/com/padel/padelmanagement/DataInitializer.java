package com.padel.padelmanagement;

import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.entity.Site;
import com.padel.padelmanagement.entity.Terrain;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.repository.SiteRepository;
import com.padel.padelmanagement.repository.TerrainRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MembreRepository membreRepository;
    private final SiteRepository siteRepository;
    private final TerrainRepository terrainRepository;

    // Injection des 3 Repositories
    public DataInitializer(MembreRepository membreRepository,
                           SiteRepository siteRepository,
                           TerrainRepository terrainRepository) {
        this.membreRepository = membreRepository;
        this.siteRepository = siteRepository;
        this.terrainRepository = terrainRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. On crée un Site et des Terrains uniquement si la table est vide
        if (siteRepository.count() == 0) {
            Site site = new Site();
            site.setNomSite("Padel Brussels Central");
            site.setHeureOuverture(LocalTime.of(8, 0));
            site.setHeureFermeture(LocalTime.of(23, 0));
            site = siteRepository.save(site); // On sauvegarde pour générer son ID

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

        // 2. On insère le membre Thomas Lefebvre
        if (!membreRepository.existsById("M7777")) {
            Membre testMembre = new Membre();
            testMembre.setMatricule("M7777");
            testMembre.setNom("Lefebvre");
            testMembre.setPrenom("Thomas");
            testMembre.setTypeMembre("Standard");
            try {
                membreRepository.save(testMembre);
                System.out.println("👉 Membre de test ('Thomas Lefebvre') inséré avec succès !");
            } catch (Exception e) {
                System.out.println("⚠️ Note : L'insertion a échoué pour M7777.");
            }
        }

        // 3. On insère le profil principal
        if (!membreRepository.existsById("C61CCAA")) {
            Membre myMembre = new Membre();
            myMembre.setMatricule("C61CCAA");
            myMembre.setNom("Galvao Coutinho");
            myMembre.setPrenom("Bryan");
            myMembre.setTypeMembre("Junior");
            try {
                membreRepository.save(myMembre);
                System.out.println("👉 Membre Bryan inséré avec succès !");
            } catch (Exception e) {
                System.out.println("⚠️ Note : L'insertion a échoué pour C61CCAA.");
            }
        }
    }
}