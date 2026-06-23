import { HttpInterceptorFn, HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth-service';

/**
 * Interceptor that adds the JWT token to all requests sent to the backend.
 * Angular runs it automatically before every HTTP request.
 *
 * If the user has a token stored in localStorage, it is added to the header:
 * Authorization: Bearer <token>
 */
export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // No token (user not logged) not modified petition
  if (!token) {
    return next(req);
  }

  const requestWithToken = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });

  return next(requestWithToken);
};