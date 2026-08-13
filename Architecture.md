Dossier d Architecture - Padel Management

1. Vue d Ensemble de l Architecture
L application Padel Management repose sur une architecture découplée N-Tier (Client-Serveur), séparant la couche de présentation du traitement métier et du stockage des données.

- Front-end : Application Single Page Application (SPA) développée avec Angular 20+.
- Back-end : API RESTful construite avec Spring Boot 4 / Java 21.
- Base de données : SGBD Relationnel Microsoft SQL Server 2022 exécuté dans un conteneur Docker dédié (padel_sql_server).

2. Infrastructure et Conteneurisation (Docker)
L environnement de données est entièrement conteneurisé afin d assurer une reproductibilité stricte entre les environnements de développement et d évaluation.

- Service SQL Server : Image officielle mcr.microsoft.com/mssql/server:2022-latest.
- Configuration des Ports : Redirection du port hôte 1445 vers le port interne 1433 du conteneur (0.0.0.0:1445->1433/tcp) pour éviter les conflits avec d autres instances SQL Server locales.
- Base de Données : Instance PadelDB initialisée au démarrage.

3. Architecture Back-end (Spring Boot)
Le back-end suit le pattern MVC / Layered Architecture avec une séparation stricte des responsabilités :

- Controller Layer (/controller) : Reçoit les requêtes HTTP, valide les données entrantes et renvoie les réponses au format JSON.
- Service Layer (/service) : Encapsule la logique métier (calculs de tarification, contraintes de dates de réservation, gestion des rôles).
- Repository Layer (/repository) : Interfaces étendant JpaRepository pour l accès et la manipulation des données en base via Spring Data JPA / Hibernate.
- Model Layer (/entity, /dto) : Représentation des entités de la base de données et objets de transfert de données.

Sécurité et Authentification
- Sécurisation stateless par JSON Web Tokens (JWT).
- Configuration Spring Security avec intercepteur de sécurité (JwtAuthenticationFilter) et gestion basée sur les rôles (ROLE_USER et ROLE_ADMIN).
- Hachage des mots de passe en base de données avec BCrypt.

Initialisation Automatique des Données (Seeding)
- Injection au démarrage via un composant DataInitializer (CommandLineRunner) créant la structure du schéma, les sites (Atomium, Sablon, Uccle, Cinquantenaire), les terrains, ainsi que les comptes de démonstration :
  * Administrateur : Thomas Lefebvre (Matricule : M7778, Mot de passe : admin123, Rôle : ROLE_ADMIN)
  * Joueur / Membre : Bryan (Matricule : C61CCAB, Mot de passe : password, Rôle : ROLE_USER)

Documentation API
- Intégration de SpringDoc OpenAPI / Swagger.
- Interface disponible après démarrage sur : http://localhost:8080/swagger-ui.html

4. Architecture Front-end (Angular)
Le front-end s appuie sur les fonctionnalités modernes du framework Angular 20+ :

- Architecture basée sur des Standalone Components réutilisables.
- Signal-based State Management : Utilisation des Angular Signals (signal, computed) pour une mise à jour réactive et performante de l interface.
- Services HTTP (/services) : Centralisation des appels API (AuthService, MatchService, MembreService, TerrainService).
- HTTP Interceptor : Injection automatique du token JWT Bearer dans l en-tête Authorization de chaque requête HTTP.
- UI Framework : Framework CSS TailwindCSS pour un design responsive et moderne.

5. Frameworks, Librairies et Outils
- Front-end : Angular 20+, RxJS, TailwindCSS, TypeScript.
- Back-end : Spring Boot 4.0+, Java 21 (JDK Temurin 21), Spring Data JPA, Spring Security, jjwt, Lombok, SpringDoc OpenAPI.
- Base de données : Microsoft SQL Server 2022, Hibernate ORM 7.x.
- Industrialisation & Outils : Maven, npm, Git, Docker, Docker Compose.
