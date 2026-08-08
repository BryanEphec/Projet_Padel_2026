import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {

  // --- 1. OBJETS DE FORMULAIRE ---
  loginData = { matricule: '', motDePasse: '' };
  registerData = { nom: '', prenom: '', email: '', motDePasse: '' };
  newMatch = { matriculeOrganisateur: '', idTerrain: '', dateMatch: '', heureDebut: '', estPrive: true };

  // --- 2. SIGNAUX D'ÉTAT (Gèrent l'affichage) ---
  currentUser = signal<any>(null);
  showAdmin = signal<boolean>(false);
  showForm = signal<boolean>(false);
  editMode = signal<boolean>(false);
  selectedMembre = signal<any>(null);

  // --- 3. SIGNAUX DE DONNÉES (Listes) ---
  terrains = signal<any[]>([]);
  matchs = signal<any[]>([]);
  membres = signal<any[]>([]);
  matchsPublics = signal<any[]>([]);
  monHistorique = signal<any[]>([]);
  historiqueMembre = signal<any[]>([]);

  constructor(private authService: AuthService, private http: HttpClient) {}

  // --- 4. MÉTHODES D'AUTHENTIFICATION ---
  login() {
    if (this.loginData.matricule && this.loginData.motDePasse) {
      this.authService.login(this.loginData.matricule, this.loginData.motDePasse).subscribe({
        next: (response) => {
          console.log('Token JWT reçu:', response.token);

          const roleAssigne = this.loginData.matricule === 'M7777' || this.loginData.matricule.startsWith('ADMIN') ? 'ROLE_ADMIN' : 'ROLE_USER';

          this.currentUser.set({
            matricule: this.loginData.matricule,
            prenom: 'Joueur',
            nom: 'Connecté',
            role: roleAssigne
          });

          this.newMatch.matriculeOrganisateur = this.loginData.matricule;
          this.chargerDonneesSimulation();
        },
        error: (err) => {
          alert('Identifiants incorrects ! Vérifiez votre matricule et mot de passe.');
        }
      });
    } else {
      alert('Veuillez remplir le matricule et le mot de passe.');
    }
  }

  register() {
    alert("Création de compte à relier à l'API Spring Boot.");
  }

  logout() {
    this.authService.logout();
    this.currentUser.set(null);
    this.showAdmin.set(false);
    this.showForm.set(false);
  }

  // --- 5. NAVIGATION DU DASHBOARD ---
  toggleAdmin() {
    this.showAdmin.set(!this.showAdmin());
    this.showForm.set(false);
  }

  toggleForm() {
    this.showForm.set(!this.showForm());
  }

  // --- 6. ACTIONS MÉTIER ---
  reserverMatch() {
    console.log("Nouvelle réservation :", this.newMatch);
    alert("Demande de réservation envoyée !");
    this.toggleForm();
  }

  getChiffreAffaires(): number {
    return this.matchs().length * 60;
  }

  voirDetails(membre: any) {
    this.selectedMembre.set(membre);
    this.editMode.set(false);
    this.historiqueMembre.set([]);
  }

  fermerDetails() {
    this.selectedMembre.set(null);
  }

  toggleEdit() {
    this.editMode.set(!this.editMode());
  }

  sauvegarderMembre() {
    alert("Modifications du joueur enregistrées !");
    this.toggleEdit();
  }

  suisJeDejaInscrit(idMatch: number): boolean {
    return false;
  }

  rejoindreMatchPublic(idMatch: number) {
    alert("Félicitations, vous avez rejoint le match !");
  }

  // --- 7. FAUSSES DONNÉES DE TEST POUR CE SOIR ---
  private chargerDonneesSimulation() {
    this.terrains.set([
      { idTerrain: 1, nomTerrain: 'Indoor 1 - Atomium (Standard)' },
      { idTerrain: 2, nomTerrain: 'Indoor 2 - Sablon (Standard)' },
      { idTerrain: 3, nomTerrain: 'Outdoor - Uccle Panoramique' }
    ]);

    this.membres.set([
      { matricule: 'M7777', nom: 'Lefebvre', prenom: 'Thomas', email: 'admin@padel.be', role: 'ROLE_ADMIN', typeMembre: 'Standard', sousPenalite: 0 },
      { matricule: 'C61CCAA', nom: 'Galvao Coutinho', prenom: 'Bryan', email: 'bryan@padel.be', role: 'ROLE_USER', typeMembre: 'Junior', sousPenalite: 0 }
    ]);

    this.matchs.set([1, 2, 3, 4, 5]);

    this.matchsPublics.set([
      { idMatch: 101, date: '2026-08-10', heure: '18:00', terrain: 'Indoor 1 - Atomium', organisateur: 'Thomas L.', nbJoueurs: 2 },
      { idMatch: 102, date: '2026-08-11', heure: '20:30', terrain: 'Outdoor - Uccle', organisateur: 'Alice B.', nbJoueurs: 3 }
    ]);

    this.monHistorique.set([
      { date: '2026-08-05', heure: '20:00', terrain: 'Outdoor - Uccle', coJoueurs: 'Thomas, Alice', aPaye: true }
    ]);
  }
}
