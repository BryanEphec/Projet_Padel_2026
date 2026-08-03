package com.padel.padelmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Clé secrète (en prod, à mettre dans application.properties)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // Durée du token (1 jour)
    private final long jwtExpirationMs = 86400000;

    // Fabrique le token
    public String generateJwtToken(String matricule, String role) {
        return Jwts.builder()
                .setSubject(matricule)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    // Lit le matricule depuis le token
    public String getMatriculeFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Vérifie si le token est valide
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            System.err.println("Token invalide: " + e.getMessage());
        }
        return false;
    }
}