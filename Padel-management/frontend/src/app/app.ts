import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MembreService } from './services/membre.service';
import { TerrainService } from './services/terrain.service';
import { MatchService } from './services/match.service';
import { AuthService } from './services/auth.service';
import { HttpClient }  from '@angular/common/http';

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
  private authService = inject(AuthService);
  private http = inject(HttpClient);

  // --- NOUVELLES VARIABLES STATS ---
  totalMembres: number = 0;
  totalReservations: number = 0;
  chiffreAffaires: number = 0;
  private adminUrl = 'http://localhost:8080/api/admin';
  membres = signal<any[]>([]);
  terrains = signal<any[]>([]);
  matchs = signal<any[]>([]);

  // Les deux variables pour les historiques
  monHistorique = signal<any[]>([]);
  matchsPublics = signal<any[]>([]);

  showForm = signal<boolean>(false);
  showAdmin = signal<boolean>(false);

  currentUser = signal<any>(null);

  loginData = { matricule: '', motDePasse: '' };
  registerData = { nom: '', prenom: '', email: '', motDePasse: '' };

  newMatch = {
    matriculeOrganisateur: '',
    idTerrain: '',
    dateMatch: '',
    heureDebut: '',
    estPrive: true
  };

  selectedMembre = signal<any>(null);
  editMode = signal<boolean>(false);
  historiqueMembre = signal<any[]>([]);

  ngOnInit() {
    this.membreService.getMembres().subscribe(data => this.membres.set(data));
    this.terrainService.getTerrains().subscribe(data => this.terrains.set(data));
    this.chargerMatchs();

    const savedUser = this.authService.getUserInfo();
    if(savedUser) {
      this.currentUser.set(savedUser);
      this.newMatch.matriculeOrganisateur = savedUser.matricule;

      this.chargerMonHistorique(savedUser.matricule);
      this.chargerMatchsPublics();
    }
  }

  // --- MÉTHODES DE CHARGEMENT DES DONNÉES ---

  chargerMatchs() {
    this.matchService.getMatches().subscribe(data => this.matchs.set(data));
  }

  chargerMonHistorique(matricule: string) {
    this.membreService.getHistorique(matricule).subscribe({
      next: (data) => this.monHistorique.set(data),
      error: (err) => console.error("Erreur historique perso :", err)
    });
  }

  chargerMatchsPublics() {
    this.matchService.getMatchsPublics().subscribe({
      next: (data) => this.matchsPublics.set(data),
      error: (err) => console.error("Erreur matchs publics :", err)
    });
  }

  chargerStatistiques() {
    this.http.get<any>(`${this.adminUrl}/stats/membres-count`).subscribe(
      data => this.totalMembres = data.totalMembres
    );
    this.http.get<any>(`${this.adminUrl}/stats/reservations-count`).subscribe(
      data => this.totalReservations = data.totalReservations
    );
    this.http.get<any>(`${this.adminUrl}/stats/chiffre-affaires`).subscribe(
      data => this.chiffreAffaires = data.chiffreAffaires
    );
  }

  // --- GESTION DES MEMBRES (ADMIN) ---

  voirDetails(membre: any) {
    this.selectedMembre.set({ ...membre });
    this.editMode.set(false);

    this.membreService.getHistorique(membre.matricule).subscribe({
      next: (data) => this.historiqueMembre.set(data),
      error: (err) => console.error("Erreur historique admin :", err)
    });
  }

  fermerDetails() {
    this.selectedMembre.set(null);
    this.editMode.set(false);
  }

  toggleEdit() {
    this.editMode.set(!this.editMode());
  }

  sauvegarderMembre() {
    this.membreService.updateMembre(this.selectedMembre().matricule, this.selectedMembre()).subscribe({
      next: () => {
        alert('Profil mis à jour avec succès !');
        this.editMode.set(false);
        this.membreService.getMembres().subscribe(data => this.membres.set(data));
      },
      error: () => alert("Erreur lors de la mise à jour")
    });
  }

  toggleAdmin() {
    this.chargerMatchs();
    this.chargerStatistiques();
    this.showAdmin.set(!this.showAdmin());
    this.showForm.set(false);
  }

  // --- CONNEXION & INSCRIPTION ---

  login() {
    this.authService.login(this.loginData.matricule, this.loginData.motDePasse).subscribe({
      next: (res) => {
        this.currentUser.set(res);
        this.newMatch.matriculeOrganisateur = res.matricule;
        this.loginData.motDePasse = '';

        this.chargerMonHistorique(res.matricule);
        this.chargerMatchsPublics();
      },
      error: () => alert("Matricule ou mot de passe incorrect.")
    });
  }

  register() {
    if(!this.registerData.nom || !this.registerData.prenom || !this.registerData.email || !this.registerData.motDePasse) {
      alert("Veuillez remplir tous les champs.");
      return;
    }
    this.membreService.inscrireMembre(this.registerData).subscribe({
      next: (newMembre) => {
        alert(`Bienvenue ! Votre matricule est : ${newMembre.matricule}. Gardez-le précieusement !`);
        this.membres.update(m => [...m, newMembre]);
        this.registerData = { nom: '', prenom: '', email: '', motDePasse: '' };
      },
      error: () => alert("Erreur lors de la création du compte.")
    });
  }

  logout() {
    this.authService.logout();
    this.currentUser.set(null);
    this.showForm.set(false);
    this.showAdmin.set(false);
    this.newMatch.matriculeOrganisateur = '';
    this.loginData.matricule = '';
  }

  // --- ACTIONS SUR LES MATCHS ---

  toggleForm() {
    this.showForm.set(!this.showForm());
    this.showAdmin.set(false);
  }

  reserverMatch() {
    let heureFormattee = this.newMatch.heureDebut;
    if (heureFormattee.length === 5) {
      heureFormattee += ":00";
    }

    const payload = {
      dateMatch: this.newMatch.dateMatch,
      heureDebut: heureFormattee,
      estPrive: this.newMatch.estPrive,
      idTerrain: Number(this.newMatch.idTerrain),
      matriculeOrganisateur: this.newMatch.matriculeOrganisateur,
      autresJoueursMatricules: []
    };

    this.matchService.creerMatch(payload).subscribe({
      next: () => {
        alert("Match réservé avec succès !");
        this.toggleForm();
        this.chargerMatchs();

        this.chargerMonHistorique(this.currentUser().matricule);
        this.chargerMatchsPublics();
      },
      error: (err) => {
        if (err.status === 400 && err.error) {
          const messages = Object.values(err.error).join('\n');
          alert("Erreur de saisie :\n" + messages);
        } else {
          alert("Erreur serveur, regarde la console !");
        }
      }
    });
  }

  rejoindreMatchPublic(idMatch: number) {
    this.matchService.rejoindreMatch(idMatch, this.currentUser().matricule).subscribe({
      next: () => {
        alert('Vous avez rejoint le match avec succès !');
        this.chargerMonHistorique(this.currentUser().matricule);
        this.chargerMatchsPublics();
        this.chargerMatchs();
      },
      error: (err) => alert(err.error || "Erreur lors de la tentative de rejoindre le match.")
    });
  }

  suisJeDejaInscrit(idMatch: number): boolean {
    return this.monHistorique().some(match => match.idMatch === idMatch);
  }
}
