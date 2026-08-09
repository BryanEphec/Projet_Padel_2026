Dossier d Architecture - Padel Management

1. Vue d Ensemble de l Architecture
L application Padel Management repose sur une architecture découplée N-Tier (Client-Serveur), séparant la couche de présentation du traitement métier et du stockage des données[cite: 1, 2].

- Front-end : Application Single Page Application (SPA) développée avec Angular 20+[cite: 1].
- Back-end : API RESTful construite avec Spring Boot 3 / Java 21[cite: 1, 2].
- Base de données : Système de Gestion de Base de Données Relationnelle Microsoft SQL Server (exécuté via Docker)[cite: 1, 2].

2. Architecture Back-end (Spring Boot)
Le back-end suit le pattern MVC / Layered Architecture avec une séparation stricte des responsabilités[cite: 2] :

- Controller Layer (/controller) : Reçoit les requêtes HTTP, valide les données entrantes et renvoie les réponses au format JSON[cite: 2].
- Service Layer (/service) : Encapsule la logique métier (calculs de tarification, contraintes de dates de réservation, gestion des rôles)[cite: 2].
- Repository Layer (/repository) : Interfaces étendant JpaRepository pour l accès et la manipulation des données en base via Spring Data JPA / Hibernate[cite: 2].
- Model Layer (/entity, /dto) : Représentation des entités de la base de données et objets de transfert de données[cite: 2].

Sécurité et Authentification
- Sécurisation stateless par JSON Web Tokens (JWT).
- Configuration Spring Security avec intercepteur de sécurité (JwtAuthentificationFilter) et gestion basée sur les rôles (ROLE_USER et ROLE_ADMIN)[cite: 1].
- Hachage des mots de passe en base de données avec BCrypt.

Documentation API
- Intégration de SpringDoc OpenAPI / Swagger.
- Interface disponible après démarrage sur : http://localhost:8080/swagger-ui.html[cite: 1]

3. Architecture Front-end (Angular)
Le front-end s appuie sur les fonctionnalités modernes du framework Angular 20+[cite: 1] :

- Architecture basée sur des Standalone Components réutilisables[cite: 1].
- Signal-based State Management : Utilisation des Angular Signals (signal, computed) pour une mise à jour réactive et performante de l interface.
- Services HTTP (/services) : Centralisation des appels API (AuthService, MatchService, MembreService, TerrainService).
- HTTP Interceptor (Vigile Interceptor) : Injection automatique du token JWT Bearer dans l entête Authorization de chaque requête.
- UI Framework : Framework CSS TailwindCSS pour un design responsive et moderne[cite: 1].

4. Frameworks, Librairies et Outils
- Front-end : Angular 20+, RxJS, TailwindCSS, TypeScript[cite: 1].
- Back-end : Spring Boot 3.5+, Java 21, Spring Data JPA, Spring Security, jjwt, Lombok, SpringDoc OpenAPI[cite: 1].
- Base de données : Microsoft SQL Server, Hibernate ORM[cite: 1, 2].
- Industrialisation & Outils : Maven, npm, Git, Docker[cite: 1].
