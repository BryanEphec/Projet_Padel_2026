package com.padel.padelmanagement.service;


import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.repository.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MembreService {

    @Autowired
    private MembreRepository membreRepository;

    public List<Membre>getAllMembre(){
        return membreRepository.findAll();
    }

    //la méthode prend mon objet Java et génère une requête SQL
    public Membre createMembre(Membre membre){
        return membreRepository.save(membre);
    }
}
