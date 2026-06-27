import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth-service';

/**
 * Route guard that blocks access to protected routes if the tutor is not authenticated (no valid JWT in localStorage).
 * Redirects to the home page if access is denied.
 */
export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  router.navigate(['/']);
  return false;
};