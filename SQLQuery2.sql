USE PadelDB;
GO

CREATE TABLE Terrains (
    ID_Terrain INT PRIMARY KEY IDENTITY(1,1), 
    Nom_Terrain NVARCHAR(50) NOT NULL,        
    ID_Site INT NOT NULL,                     

    -- Le lien de sécurité vers ta table Sites
    CONSTRAINT FK_Terrain_Site FOREIGN KEY (ID_Site) 
    REFERENCES Sites(ID_Site) 
);
GO