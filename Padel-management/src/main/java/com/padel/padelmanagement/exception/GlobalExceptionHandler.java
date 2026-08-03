package com.padel.padelmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// On utilise @RestControllerAdvice (qui combine @ControllerAdvice et @ResponseBody)
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. NOUVEAU : Intercepte les erreurs de validation (ex: @NotNull, @FutureOrPresent)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // On récupère chaque champ en erreur et on associe le message que tu as écrit dans le DTO
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        System.out.println("🛡️ [SÉCURITÉ] Requête bloquée par la validation : " + errors);

        // On renvoie un code 400 Bad Request, car c'est la faute du client (Angular)
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2. EXISTANT : Ton gestionnaire pour toutes les autres erreurs imprévues (Erreur 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e){
        System.out.println("🔥 [CRASH SERVEUR] Erreur inattendue : " + e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Une erreur technique est survenue");
        response.put("message", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}