USE PadelDB;
GO

-- 1. On crée un Site avec tes vrais champs
INSERT INTO Sites (Nom_Site, Heure_Ouverture, Heure_Fermeture) 
VALUES ('Padel Arena Bruxelles', '08:00:00', '23:00:00');

-- 2. On ajoute un Membre (On part du principe que l'ID_Site généré est 1)
-- Si tu as déjà des sites, remplace le '1' par l'ID correspondant
INSERT INTO Membres (Matricule, Nom, Prenom, Type_Membre, ID_Site_Rattachement)
VALUES ('S0001', 'Dupont', 'Jean', 'SITE', 1);

-- 3. On ajoute un Terrain rattaché à ce site via 'ID_Site'
INSERT INTO Terrains (Nom_Terrain, ID_Site)
VALUES ('Court Central', 1);

-- 4. La requête de vérification (JOIN) avec tes colonnes
SELECT 
    M.Prenom, 
    M.Nom, 
    S.Nom_Site AS Club, 
    S.Heure_Ouverture, 
    T.Nom_Terrain
FROM Membres M
JOIN Sites S ON M.ID_Site_Rattachement = S.ID_Site
JOIN Terrains T ON T.ID_Site = S.ID_Site;