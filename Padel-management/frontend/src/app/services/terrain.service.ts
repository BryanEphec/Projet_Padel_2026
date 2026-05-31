import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TerrainService {
  private http = inject(HttpClient);
  // Attention à la majuscule !
  getTerrains(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/Terrains');
  }
}
