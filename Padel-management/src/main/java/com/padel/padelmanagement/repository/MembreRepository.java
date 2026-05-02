package com.padel.padelmanagement.repository;
import com.padel.padelmanagement.entity.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembreRepository extends JpaRepository <Membre, Long> {
}
