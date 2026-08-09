import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';
  // Modifie cette URL si ta route de création est différente dans Spring Boot (ex: /api/Membres/create)
  private registerUrl = 'http://localhost:8080/api/Membres';
  constructor(private http: HttpClient) { }

  // 1. CONNEXION
  login(matricule: string, motDePasse: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { matricule, motDePasse })
      .pipe(
        tap(response => {
          if (response && response.token) {
            localStorage.setItem('jwt_token', response.token);
            const userInfo = {
              matricule: response.matricule || matricule,
              role: response.role || 'ROLE_USER',
              prenom: response.prenom || 'Membre'
            };
            localStorage.setItem('user_info', JSON.stringify(userInfo));
          }
        })
      );
  }

  // 2. INSCRIPTION (La méthode manquante !)
  register(userData: any): Observable<any> {
    // On envoie les données du formulaire directement au backend
    return this.http.post<any>(this.registerUrl, userData);
  }

  // 3. DÉCONNEXION
  logout(): void {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_info');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwt_token');
  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }

  getUserInfo(): any {
    const savedUser = localStorage.getItem('user_info');
    if (savedUser) {
      return JSON.parse(savedUser);
    }
    return null;
  }
}
