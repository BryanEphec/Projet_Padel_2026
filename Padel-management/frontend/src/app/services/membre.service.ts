import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MembreService {
  // Utilisation de inject() pour une syntaxe moderne (Angular 14+)
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/Membres';

  // Méthode pour récupérer la liste
  getMembres(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
  inscrireMembre(membre: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/Membres/register', membre);
  }
  updateMembre(matricule: string, membreData: any): Observable<any> {
    return this.http.put(`http://localhost:8080/api/Membres/${matricule}`, membreData);
  }
  getHistorique(matricule: string): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/api/Membres/${matricule}/historique`);
  }
}
