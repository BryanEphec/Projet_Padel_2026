package com.padel.padelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.padel.padelmanagement.entity.Terrain;


@Repository
public interface TerrainRepository extends JpaRepository<Terrain, Long> {

}
