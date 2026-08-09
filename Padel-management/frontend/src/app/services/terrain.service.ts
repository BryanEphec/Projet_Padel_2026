import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TerrainService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/Terrains'; // ou /api/Terrains selon ton backend

  getTerrains(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
