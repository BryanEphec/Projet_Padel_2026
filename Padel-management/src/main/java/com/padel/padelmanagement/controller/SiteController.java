package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.service.SiteService;
import com.padel.padelmanagement.entity.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Informe Spring que cette classe est une API qui renvoie du JSON
@RequestMapping("/api/sites") // Définit l'URL de base : http://localhost:8080/api/sites
@CrossOrigin(origins = "http://localhost:4200") // Autorise ton futur projet Angular à appeler cette API
public class SiteController {

    @Autowired // Branche automatiquement le service (l'injection dont on a parlé)
    private SiteService siteService;

    /**
     * Cette méthode s'active lorsqu'on consulte l'URL en mode "Lecture" (GET)
     * Elle demande au service la liste des sites et la renvoie au format JSON
     */
    @GetMapping
    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }
}
