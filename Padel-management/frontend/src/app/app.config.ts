import { ApplicationConfig, inject } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors, HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from './services/auth.service';

// Voici l'intercepteur magique qui ajoute le token à chaque requête !
const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const user = authService.getUserInfo();

  // On récupère le token selon comment ton backend l'a nommé
  const token = user ? (user.token || user.accessToken) : null;

  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }
  return next(req);
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])) // On active l'intercepteur ici
  ]
};
