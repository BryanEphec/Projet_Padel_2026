import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MembreService } from './services/membre.service';
import { TerrainService } from './services/terrain.service';
import { MatchService } from './services/match.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html'
})
export class AppComponent implements OnInit {
  private membreService = inject(MembreService);
  private terrainService = inject(TerrainService);
  private matchService = inject(MatchService);

  membres = signal<any[]>([]);
  terrains = signal<any[]>([]);
  matchs = signal<any[]>([]); // NOUVEAU : Stocke la liste des matchs

  showForm = signal<boolean>(false);
  showAdmin = signal<boolean>(false); // NOUVEAU : Gère l'affichage de l'admin

  // --- GESTION DE LA SESSION ---
  currentUser = signal<any>(null);

  loginData = { matricule: '' };
  registerData = { nom: '', prenom: '' };

  newMatch = {
    matriculeOrganisateur: '',
    idTerrain: '',
    dateMatch: '',
    heureDebut: '',
    estPrive: true
  };

  ngOnInit() {
    // On charge les membres
    this.membreService.getMembres().subscribe({
      next: (data) => this.membres.set(data),
      error: (err) => console.error('Erreur Membres :', err)
    });

    // On charge les terrains
    this.terrainService.getTerrains().subscribe({
      next: (data) => this.terrains.set(data),
      error: (err) => console.error('Erreur Terrains :', err)
    });

    // NOUVEAU : On charge l'historique des matchs
    this.chargerMatchs();
  }

  // --- NOUVEAUTÉ : MÉTHODES ADMIN ---
  chargerMatchs() {
    this.matchService.getMatches().subscribe({
      next: (data) => this.matchs.set(data),
      error: (err) => console.error('Erreur Matchs :', err)
    });
  }

  toggleAdmin() {
    this.chargerMatchs(); // Rafraîchit les données
    this.showAdmin.set(!this.showAdmin());
    this.showForm.set(false); // Cache le formulaire de réservation
  }

  getChiffreAffaires() {
    // 60€ par match réservé
    return this.matchs().length * 60;
  }
  // ----------------------------------

  // --- MÉTHODES DE CONNEXION / INSCRIPTION ---
  login() {
    const user = this.membres().find(m => m.matricule === this.loginData.matricule);
    if (user) {
      this.currentUser.set(user);
      this.newMatch.matriculeOrganisateur = user.matricule;
    } else {
      alert("Matricule introuvable. Vérifiez votre saisie ou créez un compte.");
    }
  }

  register() {
    if(!this.registerData.nom || !this.registerData.prenom) {
      alert("Veuillez remplir votre nom et prénom.");
      return;
    }

    this.membreService.inscrireMembre(this.registerData).subscribe({
      next: (newMembre) => {
        alert(`Bienvenue ! Votre matricule est : ${newMembre.matricule}. Gardez-le précieusement !`);
        this.membres.update(m => [...m, newMembre]);
        this.currentUser.set(newMembre);
        this.newMatch.matriculeOrganisateur = newMembre.matricule;
      },
      error: (err) => {
        console.error("Erreur inscription", err);
        alert("Erreur lors de la création du compte.");
      }
    });
  }

  logout() {
    this.currentUser.set(null);
    this.showForm.set(false);
    this.showAdmin.set(false); // NOUVEAU : Ferme l'admin à la déconnexion
    this.newMatch.matriculeOrganisateur = '';
    this.loginData.matricule = '';
  }

  toggleForm() {
    this.showForm.set(!this.showForm());
    this.showAdmin.set(false); // NOUVEAU : Cache l'admin si on ouvre le form
  }

  reserverMatch() {
    const payload = {
      dateMatch: this.newMatch.dateMatch,
      heureDebut: this.newMatch.heureDebut + ":00",
      estPrive: this.newMatch.estPrive,
      idTerrain: Number(this.newMatch.idTerrain),
      matriculeOrganisateur: this.newMatch.matriculeOrganisateur,
      autresJoueursMatricules: []
    };

    this.matchService.creerMatch(payload).subscribe({
      next: (res) => {
        alert("Match réservé avec succès !");
        this.toggleForm();
        this.chargerMatchs(); // NOUVEAU : Met à jour le CA et la liste !
      },
      error: (err) => {
        console.error('Détails erreur:', err);
        alert("Erreur serveur, regarde la console !");
      }
    });
  }
}
