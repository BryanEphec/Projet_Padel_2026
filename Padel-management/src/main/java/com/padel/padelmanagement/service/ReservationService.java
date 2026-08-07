package com.padel.padelmanagement.service;
import com.padel.padelmanagement.exception.RegleMetierException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
public class ReservationService {
    public void verifierDroitsSelonMatricule(String matricule, LocalDate dateMatch){
        long joursAvantMatch = ChronoUnit.DAYS.between(LocalDate.now(), dateMatch);

        if(matricule.toUpperCase().startsWith("G")){
            if(joursAvantMatch > 21){
                throw new RegleMetierException("Les membres Globaux (G) ne peuvent réserver que 3 semaines à l'avance maximum.");
            }
        }
        else if(matricule.toUpperCase().startsWith("S")){
            if(joursAvantMatch > 14){
                throw new RegleMetierException("Les membres de Site (S) ne peuvent réserver que 2 semaines à l'avance maximum.");
            }
        }
        else if(matricule.toUpperCase().startsWith("L")){
            if(joursAvantMatch > 5){
                throw new RegleMetierException("Les membres Libre (L) ne peuvent réserver que 5 jours à l'avance maximum.");
            }
        }
        else {
            throw new RegleMetierException("Type de matricule non reconnu. Il doit commencer par G, S ou L.");
        }
    }
}
