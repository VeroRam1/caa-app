import { ApplicationConfig, provideBrowserGlobalErrorListeners, isDevMode } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { provideServiceWorker } from '@angular/service-worker';
import { providePrimeNG } from 'primeng/config';
import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';
import { authInterceptor } from './interceptors/auth.interceptors';

// Maps the app's blue palette onto PrimeNG's primary token scale so all
// components (buttons, focus rings, etc.) use the app's brand colours.
const PictoComPreset = definePreset(Aura, {
  semantic: {
    primary: {
      50:  '#e8f4fd',
      100: '#b3ddff',
      200: '#6eb3f3',
      300: '#6eb3f3',
      400: '#4a85b5',
      500: '#4a85b5',
      600: '#1a4971',
      700: '#1a4971',
      800: '#0c2340',
      900: '#0c2340',
      950: '#0c2340',
    }
  }
});

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideServiceWorker('ngsw-worker.js', {
            enabled: !isDevMode(),
            registrationStrategy: 'registerWhenStable:30000'
          }),
    provideHttpClient(withInterceptors([authInterceptor])),
    providePrimeNG({
            theme: {
                preset: PictoComPreset,
                // '.p-dark' never appears in the DOM → light tokens always win
                options: { darkModeSelector: '.p-dark' }
            }
        })
  ]
};
