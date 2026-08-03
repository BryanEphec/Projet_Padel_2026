import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/auth';

  login(matricule: string, motDePasse: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { matricule, motDePasse }).pipe(
      tap((res: any) => {
        // Dès qu'on reçoit le token, on le sauvegarde dans le navigateur !
        if (res && res.token) {
          localStorage.setItem('jwt_token', res.token);
          // On peut aussi stocker les infos de l'utilisateur pour y accéder facilement
          localStorage.setItem('user_info', JSON.stringify(res));
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_info');
  }

  getToken() {
    return localStorage.getItem('jwt_token');
  }

  getUserInfo() {
    const info = localStorage.getItem('user_info');
    return info ? JSON.parse(info) : null;
  }
}
