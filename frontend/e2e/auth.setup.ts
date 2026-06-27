import { test as setup, expect } from '@playwright/test';

const AUTH_FILE = 'e2e/auth.json';
const EMAIL = 'tutor@pictocom.com';
const PASSWORD = 'password123';

setup('create authentication state', async ({ page }) => {
  await page.goto('/');

  // Try login first
  await page.getByRole('button', { name: 'Iniciar sesión con tu cuenta' }).click();
  await page.getByRole('textbox', { name: 'Correo electrónico' }).fill(EMAIL);
  await page.getByRole('textbox', { name: 'Contraseña' }).fill(PASSWORD);
  await page.getByRole('button', { name: 'Entrar', exact: true }).click();

  const loginFailed = await page.getByRole('alert').isVisible({ timeout: 3000 }).catch(() => false);

  if (loginFailed) {
    // Account doesn't exist — register it
    await page.getByRole('button', { name: 'Cancelar' }).click();
    await page.getByRole('button', { name: 'Crear una cuenta nueva' }).click();

    await page.getByRole('textbox', { name: 'Nombre completo' }).fill('Test Tutor');
    await page.getByRole('textbox', { name: 'Correo electrónico' }).fill(EMAIL);
    await page.getByRole('textbox', { name: 'Contraseña', exact: true }).fill(PASSWORD);
    await page.getByRole('textbox', { name: 'Confirmar contraseña' }).fill(PASSWORD);
    await page.getByRole('button', { name: 'Registrarse', exact: true }).click();
  }

  await page.waitForURL('/dashboard');
  await expect(page.getByRole('heading', { name: /Bienvenido/ })).toBeVisible();

  await page.context().storageState({ path: AUTH_FILE });
});
