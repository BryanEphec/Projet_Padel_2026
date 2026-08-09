Document d Exploitation et de Déploiement - Padel Management

Ce document détaille l ensemble des étapes nécessaires pour compiler, configurer, exécuter et tester l application Padel Management[cite: 1].

1. Prérequis Système
Avant de commencer, assurez-vous de disposer des éléments suivants sur votre environnement[cite: 1] :
- Node.js (v20+) et npm[cite: 1]
- Java JDK 21+[cite: 1]
- Docker Desktop (pour la base de données SQL Server)[cite: 1]
- Maven 3.8+[cite: 1]

2. Base de Données et Initialisation (Seeding)

Démarrage de la Base de Données
Le SGBD s exécute dans un conteneur Docker. Pour lancer la base de données :
docker compose up -d

Informations de connexion :
- Hôte : 127.0.0.1
- Port : 1440
- Base de données : PadelDB
- Utilisateur : sa

Jeux de données de test (DataSeeder)
Au démarrage de l application back-end, la classe DataSeeder initialise automatiquement la base de données si celle-ci est vide (création des terrains et comptes de démonstration)[cite: 1].

3. Procédure de Lancement du Back-end (Spring Boot)

Port par défaut : 8080[cite: 1]

1. Compilation du projet :
mvn clean install -DskipTests

2. Lancement de l application :
mvn spring-boot:run

3. Exécution des tests automatisés back-end :
mvn test[cite: 1]

4. Procédure de Lancement du Front-end (Angular)

Port par défaut : 4200[cite: 1]

1. Installation des dépendances :
npm install

2. Démarrage du serveur de développement :
npm start

3. Exécution des tests automatisés front-end :
npm test[cite: 1]

5. Guide de Démonstration pour l Évaluation

L accès à l application se fait depuis un navigateur web à l adresse : http://localhost:4200

Procédure de test immédiate :
1. Sur l écran d accueil, utilisez le formulaire "Nouveau Joueur ?" pour créer un compte.
2. Le système attribue automatiquement un matricule (ex: Sxxxx), effectue la connexion automatique et affiche l espace réservations.
3. Pour tester l administration, un compte administrateur peut être créé directement ou géré via le rôle ROLE_ADMIN en base de données.
