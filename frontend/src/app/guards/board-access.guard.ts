import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth-service';

/**
 * Route guard for /board/:id.
 * - Guest mode (?mode=guest): always allowed, no login required.
 * - Tutor mode (?mode=tutor): requires authentication.
 * - Child mode (no mode param): requires authentication AND an active profile.
 */
export const boardAccessGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const mode = route.queryParamMap.get('mode');

  // Guest mode is always allowed
  if (mode === 'guest') {
    return true;
  }

  // Tutor mode and child mode both require authentication
  if (!authService.isLoggedIn()) {
    router.navigate(['/']);
    return false;
  }

  // Tutor mode: authenticated is enough
  if (mode === 'tutor') {
    return true;
  }

  // Child mode: also requires an active profile selected
  const activeProfileId = authService.getActiveProfileId();
  if (!activeProfileId) {
    router.navigate(['/dashboard']);
    return false;
  }

  return true;
};