import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MatchService {
  private http = inject(HttpClient);

  // 👉 LA CORRECTION EST ICI : On déclare l'URL de base pour tout le service
  private apiUrl = 'http://localhost:8080/api/Matches';

  creerMatch(matchData: any): Observable<any> {
    return this.http.post(this.apiUrl, matchData);
  }

  getMatches(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getMatchsPublics(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/publics`);
  }

  rejoindreMatch(idMatch: number, matricule: string): Observable<any> {
    // On met responseType: 'text' car notre backend renvoie une simple phrase (String) en cas de succès, pas un JSON
    return this.http.post(`${this.apiUrl}/${idMatch}/join`, { matricule }, { responseType: 'text' });
  }
}
