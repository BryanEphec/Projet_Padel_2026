package com.padel.padelmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI padelOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Padel Management SaaS")
                        .description("Documentation interactive de l'API REST sécurisée pour la gestion des réservations, des terrains et des membres du centre de Padel.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bryan Galvao Coutinho")
                                .email("admin@padelmanagement.be")));
    }
}