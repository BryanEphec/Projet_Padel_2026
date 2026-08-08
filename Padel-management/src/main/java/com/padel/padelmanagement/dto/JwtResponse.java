package com.padel.padelmanagement.dto;

public class JwtResponse {
    private String token;
    private String matricule;
    private String role;

    public JwtResponse(String token, String matricule, String role) {
        this.token = token;
        this.matricule = matricule;
        this.role = role;

    }

    public String getToken() {
        return token;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getRole() {
        return role;
    }
}
