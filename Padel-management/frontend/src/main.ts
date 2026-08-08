import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app';
import { provideHttpClient, withInterceptors, HttpInterceptorFn } from '@angular/common/http';

const authInterceptor: HttpInterceptorFn = (req, next) => {
  // On va chercher EXACTEMENT le nom que tu as utilisé dans ton AuthService !
  const token = localStorage.getItem('jwt_token');

  if (token) {
    console.log("✅ Vigile Interceptor : Jeton 'jwt_token' trouvé ! Passage autorisé.");
    const cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(cloned);
  }

  console.log("❌ Vigile Interceptor : Pas de jeton trouvé, requête envoyée nue.");
  return next(req);
};

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
}).catch(err => console.error(err));
