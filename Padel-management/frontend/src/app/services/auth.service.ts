import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // L'URL de ton backend Spring Boot (ajuste le port si ce n'est pas 8080)
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  // 1. Méthode pour se connecter
  login(matricule: string, motDePasse: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { matricule, motDePasse })
      .pipe(
        tap(response => {
          // Si on reçoit un token, on le sauvegarde dans le navigateur
          if (response && response.token) {
            localStorage.setItem('jwt_token', response.token);
          }
        })
      );
  }

  // 2. Méthode pour se déconnecter
  logout(): void {
    localStorage.removeItem('jwt_token');
  }

  // 3. Vérifier si l'utilisateur est connecté (s'il possède un token)
  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwt_token');
  }

  // 4. Récupérer le token pour l'ajouter aux futures requêtes
  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }
  // À ajouter dans AuthService
  getUserInfo(): any {
    return { prenom: 'Test', role: 'ROLE_USER' };
  }
}
