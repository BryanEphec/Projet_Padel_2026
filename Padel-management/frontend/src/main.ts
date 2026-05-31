import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app'; // Attention à l'import ici !

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
