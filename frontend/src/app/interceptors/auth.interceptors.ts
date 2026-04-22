import { HttpInterceptorFn, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth-service';

/**
 * Interceptor que añade el token JWT a todas las peticiones al backend.
 * Angular lo ejecuta automáticamente antes de cada petición HTTP.
 *
 * Si el usuario tiene token guardado en localStorage, lo añade en el header:
 * Authorization: Bearer <token>
 */
export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Si no hay token (usuario no logueado) dejamos la petición sin modificar
  if (!token) {
    return next(req);
  }

  // Clonamos la petición añadiendo el header Authorization
  // Las peticiones HTTP son inmutables en Angular — hay que clonarlas para modificarlas
  const requestWithToken = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });

  return next(requestWithToken);
};