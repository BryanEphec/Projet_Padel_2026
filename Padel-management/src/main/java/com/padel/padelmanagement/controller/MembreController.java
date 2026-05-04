package com.padel.padelmanagement.controller;


import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.service.MembreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Membre")// Définit l'url de base http://localhost:8080/api/Membre
@CrossOrigin(origins = "http://localhost:4200")// autorise angular à appeler cette API
public class MembreController {

    @Autowired
    private MembreService membreService;

    @GetMapping
    public List<Membre> getAllMembre(){
        return membreService.getAllMembre();
    }
}
