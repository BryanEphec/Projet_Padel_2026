import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common'; // Important pour *ngFor
import { MembreService } from './services/membre.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule], // Ajoute CommonModule pour utiliser les directives Angular
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class AppComponent implements OnInit {
  private membreService = inject(MembreService);
  membres = signal<any[]>([]); // Ton signal réactif

  ngOnInit() {
    this.membreService.getMembres().subscribe({
      next: (data) => this.membres.set(data),
      error: (err) => console.error('Erreur API :', err)
    });
  }
}
