import { test, expect } from '@playwright/test';

test.describe('Authentication', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('home page shows login, register and guest buttons', async ({ page }) => {
    await expect(page.getByRole('button', { name: 'Iniciar sesión con tu cuenta' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Crear una cuenta nueva' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Entrar como invitado sin registrarse' })).toBeVisible();
  });

  test('login button opens dialog with email and password fields', async ({ page }) => {
    await page.getByRole('button', { name: 'Iniciar sesión con tu cuenta' }).click();

    const dialog = page.getByRole('dialog', { name: 'Iniciar sesión' });
    await expect(dialog).toBeVisible();
    await expect(dialog.getByRole('textbox', { name: 'Correo electrónico' })).toBeVisible();
    await expect(dialog.getByRole('textbox', { name: 'Contraseña' })).toBeVisible();
  });

  test('login submit button is disabled when fields are empty', async ({ page }) => {
    await page.getByRole('button', { name: 'Iniciar sesión con tu cuenta' }).click();

    await expect(page.getByRole('button', { name: 'Entrar', exact: true })).toBeDisabled();
  });

  test('invalid credentials show error message', async ({ page }) => {
    await page.getByRole('button', { name: 'Iniciar sesión con tu cuenta' }).click();
    await page.getByRole('textbox', { name: 'Correo electrónico' }).fill('nobody@example.com');
    await page.getByRole('textbox', { name: 'Contraseña' }).fill('wrongpassword');
    await page.getByRole('button', { name: 'Entrar', exact: true }).click();

    await expect(page.getByRole('alert')).toContainText('El email o la contraseña no son correctos');
    await expect(page).toHaveURL('/');
  });

  test('valid credentials redirect to dashboard', async ({ page }) => {
    await page.getByRole('button', { name: 'Iniciar sesión con tu cuenta' }).click();
    await page.getByRole('textbox', { name: 'Correo electrónico' }).fill('tutor@pictocom.com');
    await page.getByRole('textbox', { name: 'Contraseña' }).fill('password123');
    await page.getByRole('button', { name: 'Entrar', exact: true }).click();

    await page.waitForURL('/dashboard');
    await expect(page.getByRole('heading', { name: /Bienvenido/ })).toBeVisible();
  });

  test('register button opens dialog with name, email and password fields', async ({ page }) => {
    await page.getByRole('button', { name: 'Crear una cuenta nueva' }).click();

    const dialog = page.getByRole('dialog', { name: 'Crear cuenta' });
    await expect(dialog).toBeVisible();
    await expect(dialog.getByRole('textbox', { name: 'Nombre completo' })).toBeVisible();
    await expect(dialog.getByRole('textbox', { name: 'Correo electrónico' })).toBeVisible();
    await expect(dialog.getByRole('textbox', { name: 'Contraseña', exact: true })).toBeVisible();
    await expect(dialog.getByRole('textbox', { name: 'Confirmar contraseña' })).toBeVisible();
  });

  test('registration with a new account redirects to dashboard', async ({ page }) => {
    const uniqueEmail = `test_${Date.now()}@example.com`;

    await page.getByRole('button', { name: 'Crear una cuenta nueva' }).click();
    await page.getByRole('textbox', { name: 'Nombre completo' }).fill('New Tutor');
    await page.getByRole('textbox', { name: 'Correo electrónico' }).fill(uniqueEmail);
    await page.getByRole('textbox', { name: 'Contraseña', exact: true }).fill('password123');
    await page.getByRole('textbox', { name: 'Confirmar contraseña' }).fill('password123');
    await page.getByRole('button', { name: 'Registrarse', exact: true }).click();

    await page.waitForURL('/dashboard');
    await expect(page.getByRole('heading', { name: /Bienvenido/ })).toBeVisible();
  });
});
