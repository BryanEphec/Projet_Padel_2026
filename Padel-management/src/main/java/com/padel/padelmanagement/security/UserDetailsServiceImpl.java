package com.padel.padelmanagement.security;

import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.repository.MembreRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MembreRepository membreRepository;

    public UserDetailsServiceImpl(MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String matricule) throws UsernameNotFoundException {
        // Va chercher le joueur dans la base de données
        Membre membre = membreRepository.findById(matricule)
                .orElseThrow(() -> new UsernameNotFoundException("Joueur non trouvé avec le matricule: " + matricule));

        // Construit l'objet User que Spring Security comprend
        return new User(
                membre.getMatricule(),
                membre.getMotDePasse(),
                Collections.singletonList(new SimpleGrantedAuthority(membre.getRole()))
        );
    }
}