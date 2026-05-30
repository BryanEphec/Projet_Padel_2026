Projet Padel Management 2026 - Dossier d'Architecture & d'Exploitation

## 1. Dossier d'Architecture

L'application respecte une architecture web découplée en couches (N-Tier Architecture), garantissant la séparation des responsabilités.

### Back-end (Spring Boot)
* **Version :** Java 21, Spring Boot 4.0.4
* **Couche Controller :** Expose l'API REST (`/api/Membre`) et intercepte les requêtes HTTP. Gère les codes de statut standard (200, 201, 401, 403).
* **Couche Service :** Contient la logique métier applicative.
* **Couche Repository :** Abstraction des accès à la base de données via Spring Data JPA.
* **Sécurité :** Simulation de contrôle d'accès basé sur des rôles via interception de Token Bearer (Rôles implémentés : Admin / Membre).

### Front-end (Angular)
* **Version :** Angular 20+
* **Architecture :** Composants réutilisables, Services centralisés pour les appels HTTP via `HttpClient`, et gestion d'état réactive basée sur les **Angular Signals**.

## 2. Document d'Exploitation & Déploiement

### Prérequis applicatifs
* Docker Desktop installé et configuré.
* Aucun script SQL manuel n'est nécessaire (Industrialisation complète).

### Étape 1 : Démarrage de l'infrastructure (Base de données)
La base de données s'exécute de manière isolée dans un conteneur Docker. Pour la démarrer :
```bash
docker compose up -d
