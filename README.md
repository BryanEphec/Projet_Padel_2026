Document d Exploitation et de Déploiement - Padel Management

Ce document détaille l ensemble des étapes nécessaires pour compiler, configurer, exécuter et tester l application Padel Management.

1. Prérequis Système
Avant de commencer, assurez-vous de disposer des éléments suivants sur votre environnement :
- Node.js (v20+) et npm
- Java JDK 21+
- Docker Desktop (pour la base de données SQL Server)
- Maven 3.8+

2. Base de Données et Initialisation (Seeding)

Démarrage de la Base de Données
Le SGBD s exécute dans un conteneur Docker (padel_sql_server). Pour lancer la base de données :
docker compose up -d

Afin de créer l instance PadelDB au premier lancement avant le démarrage d Hibernate, exécuter :
docker exec -it padel_sql_server /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "NouveauMotDePasse123!" -C -Q "CREATE DATABASE PadelDB;"

Informations de connexion :
- Hôte : 127.0.0.1
- Port : 1445 (port interne 1433)
- Base de données : PadelDB
- Utilisateur : sa
- Mot de passe : NouveauMotDePasse123!

Jeux de données de test (DataSeeder)
Au démarrage du back-end, la classe d'initialisation (CommandLineRunner / DataInitializer) génère automatiquement la structure des tables via Hibernate ainsi que les données de démonstration :
- Sites de Padel : Atomium, Sablon, Uccle, Cinquantenaire.
- Terrains : Terrains associés aux différents sites.
- Comptes de démonstration pré-créés :
  - Administrateur : Thomas Lefebvre 
    * Identifiant / Matricule : M7778
    * Mot de passe : admin123
    * Rôle : ROLE_ADMIN
  - Joueur / Membre : Bryan
    * Identifiant / Matricule : C61CCAB
    * Mot de passe : password
    * Rôle : ROLE_USER

3. Procédure de Lancement du Back-end (Spring Boot)

Port par défaut : 8080

1. Compilation du projet et exécution des tests :
mvn clean install

2. Lancement de l application :
mvn spring-boot:run

3. Exécution des tests automatisés back-end :
mvn test

4. Procédure de Lancement du Front-end (Angular)

Port par défaut : 4200

1. Installation des dépendances :
npm install

2. Démarrage du serveur de développement :
npm start

3. Exécution des tests automatisés front-end :
mvn test

5. Guide de Démonstration pour l Évaluation

L accès à l application se fait depuis un navigateur web à l adresse : http://localhost:4200

Procédure de test immédiate :
1. Accès Administrateur : Connectez-vous directement avec le compte administrateur généré au seeding (Matricule : M7778 / Mot de passe : admin123) pour gérer les sites, fermetures et terrains sans aucune manipulation manuelle en base de données.
2. Accès Joueur : Connectez-vous avec le compte joueur (Matricule : C61CCAB / Mot de passe : password) ou utilisez le formulaire "Nouveau Joueur ?" sur l écran d accueil pour inscrire un utilisateur à chaud.
3. Réservations : Le système attribue automatiquement un matricule (ex: Sxxxx), effectue la connexion automatique et affiche l espace réservations.
