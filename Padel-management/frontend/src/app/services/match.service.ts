import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MatchService {
  private http = inject(HttpClient);

  creerMatch(matchData: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/Matches', matchData);
  }
  getMatches(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/Matches');
  }
}
