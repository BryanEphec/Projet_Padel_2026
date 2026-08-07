package com.padel.padelmanagement.service;

import com.padel.padelmanagement.entity.Match;
import com.padel.padelmanagement.entity.Participation;
import com.padel.padelmanagement.repository.MatchRepository;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchSchedulerService {

    @Autowired private MatchRepository matchRepository;
    @Autowired private ParticipationRepository participationRepository;
    @Autowired private MembreRepository membreRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void verifierMatchsPrivesDeLaVeille(){
        System.out.println(" [CRON] Vérification des matchs privés pour el lendemain ....");

        LocalDate demain = LocalDate.now().plusDays(1);

        List<Match> matchsDeDemain = matchRepository.findAll().stream().filter(m -> m.getDateMatch() != null && m.getDateMatch().equals(demain)).toList();

        for(Match match : matchsDeDemain){
            if(Boolean.TRUE.equals(match.getEstPrive())){
                List<Participation> participations = participationRepository.findByMatch_IdMatch(match.getIdMatch());

                if(participations.size()< 4){
                    System.out.println(" Match "+ match.getIdMatch()+ " incomplet. Passage en PUBLIC");
                    match.setEstPrive(false);
                    matchRepository.save(match);

                participations.stream().filter(p -> Boolean.TRUE.equals(p.getEstOrganisateur())).findFirst().ifPresent(orgaPart ->{
                    var orga = orgaPart.getMembre();
                    orga.setSousPenalite(orga.getSousPenalite()+1);
                    membreRepository.save(orga);
                    System.out.println(" Pénalité appliquée à l'organisateur : "+ orga.getMatricule());
                });
                }
            }
        }
        System.out.println("[CRON] vérification terminée.");
    }
}
