USE PadelDB; -- 1. Toujours vérifier qu'on est dans la bonne base
GO

CREATE TABLE Membres (
    Matricule NVARCHAR(10) PRIMARY KEY,    -- Gxxxx, Sxxxxx, Lxxxxx
    Nom NVARCHAR(100) NOT NULL,
    Prenom NVARCHAR(100) NOT NULL,
    Type_Membre NVARCHAR(20) NOT NULL,      -- 'Global', 'Site' ou 'Libre'
    ID_Site_Rattachement INT NULL,          -- NULL autorisé pour les 'Libres'
    
    -- 2. On crée le lien avec la table Sites
    CONSTRAINT FK_Membre_Site FOREIGN KEY (ID_Site_Rattachement) 
    REFERENCES Sites(ID_Site)
);
GO