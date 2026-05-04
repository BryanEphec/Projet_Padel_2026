package com.padel.padelmanagement.service;


import com.padel.padelmanagement.entity.Terrain;
import com.padel.padelmanagement.repository.TerrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerrainService {

    @Autowired
    private TerrainRepository terrainRepository;

    public List<Terrain>getAllTerrain(){
        return terrainRepository.findAll();
    }
}
