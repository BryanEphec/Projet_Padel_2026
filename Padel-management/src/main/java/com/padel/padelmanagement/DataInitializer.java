package com.padel.padelmanagement;

import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.repository.MembreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MembreRepository membreRepository;

    public DataInitializer(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // On force l'insertion d'un membre de test pour le prof ce soir
        Membre testMembre = new Membre();
        testMembre.setMatricule("M7777");
        testMembre.setNom("Lefebvre");
        testMembre.setPrenom("Thomas");
        testMembre.setTypeMembre("Standard");


        try {
            membreRepository.save(testMembre);
            System.out.println("👉 Membre de test ('Thomas Lefebvre') insere avec succes !");
        } catch (Exception e) {
            System.out.println("⚠️ Note : L'insertion a echoue ou le membre existe deja.");
        }
    }
}