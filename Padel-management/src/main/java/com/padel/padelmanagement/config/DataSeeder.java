package com.padel.padelmanagement.config;

import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.entity.Terrain;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.repository.TerrainRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(MembreRepository membreRepository,
                                   TerrainRepository terrainRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {

            // 1. On vérifie si la table Membre est vide pour ne pas créer de doublons à chaque démarrage
            if (membreRepository.count() == 0) {
                System.out.println("🌱 Initialisation des données de test (Membres)...");

                // Création de l'Administrateur
                Membre admin = new Membre();
                admin.setMatricule("G1000");
                admin.setNom("Boss");
                admin.setPrenom("Hugo");
                admin.setEmail("admin@padel.be");
                admin.setMotDePasse(passwordEncoder.encode("admin123")); // Le mot de passe sera bien crypté !
                admin.setRole("ROLE_ADMIN");
                admin.setTypeMembre("VIP");
                admin.setSousPenalite(0);
                membreRepository.save(admin);

                // Création d'un joueur classique de test
                Membre joueur = new Membre();
                joueur.setMatricule("S1000");
                joueur.setNom("Doe");
                joueur.setPrenom("John");
                joueur.setEmail("john@padel.be");
                joueur.setMotDePasse(passwordEncoder.encode("joueur123"));
                joueur.setRole("ROLE_USER");
                joueur.setTypeMembre("Standard");
                joueur.setSousPenalite(0);
                membreRepository.save(joueur);

                System.out.println("✅ Membres de test créés avec succès !");
            }

            // 2. On vérifie si la table Terrain est vide
            if (terrainRepository.count() == 0) {
                System.out.println("🌱 Initialisation des terrains...");

                Terrain t1 = new Terrain();
                t1.setNomTerrain("Terrain Panoramique 1");
                terrainRepository.save(t1);

                Terrain t2 = new Terrain();
                t2.setNomTerrain("Terrain Indoor 2");
                terrainRepository.save(t2);

                Terrain t3 = new Terrain();
                t3.setNomTerrain("Terrain Outdoor 3");
                terrainRepository.save(t3);

                System.out.println("✅ Terrains créés avec succès !");
            }

            System.out.println("🚀 L'application est prête avec son jeu de données !");
        };
    }
}