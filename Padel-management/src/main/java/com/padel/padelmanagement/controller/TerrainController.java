package com.padel.padelmanagement.controller;


import com.padel.padelmanagement.entity.Terrain;
import com.padel.padelmanagement.service.TerrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Terrains")
@CrossOrigin(origins = "http://localhost:4200")
public class TerrainController {

    @Autowired
    private TerrainService terrainService;

    @GetMapping
    public List<Terrain> getAllTerrain(){
        return terrainService.getAllTerrain();
    }
}
