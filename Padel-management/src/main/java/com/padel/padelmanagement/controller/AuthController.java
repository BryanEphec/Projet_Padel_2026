package com.padel.padelmanagement.controller;

import com.padel.padelmanagement.entity.Membre;
import com.padel.padelmanagement.repository.MembreRepository;
import com.padel.padelmanagement.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private MembreRepository membreRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static class LoginRequest {
        public String matricule;
        public String motDePasse;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Optional<Membre> membreOpt = membreRepository.findById(loginRequest.matricule);

        if (membreOpt.isPresent()) {
            Membre membre = membreOpt.get();

            if (membre.getMotDePasse() != null && passwordEncoder.matches(loginRequest.motDePasse, membre.getMotDePasse())) {

                String jwt = jwtUtils.generateJwtToken(membre.getMatricule(), membre.getRole());

                Map<String, Object> response = new HashMap<>();
                response.put("token", jwt);
                response.put("matricule", membre.getMatricule());
                response.put("role", membre.getRole());
                response.put("prenom", membre.getPrenom());
                response.put("nom", membre.getNom());

                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Matricule ou mot de passe incorrect");
    }
}