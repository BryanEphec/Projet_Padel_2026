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
}
