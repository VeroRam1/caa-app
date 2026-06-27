import { test, expect } from '@playwright/test';

test.describe('Guest mode', () => {
  test('guest button from home navigates to /guest-page', async ({ page }) => {
    await page.goto('/');
    await page.getByRole('button', { name: 'Entrar como invitado sin registrarse' }).click();

    await expect(page).toHaveURL('/guest-page');
  });

  test('guest page shows level selection heading', async ({ page }) => {
    await page.goto('/guest-page');

    await expect(page.getByRole('heading', { name: 'Elige un nivel', level: 2 })).toBeVisible();
  });

  test('guest page shows three level cards (Level 1, 2 and 3)', async ({ page }) => {
    await page.goto('/guest-page');

    await expect(page.getByRole('heading', { name: 'Nivel 1', level: 3 })).toBeVisible();
    await expect(page.getByRole('heading', { name: 'Nivel 2', level: 3 })).toBeVisible();
    await expect(page.getByRole('heading', { name: 'Nivel 3', level: 3 })).toBeVisible();
  });

  test('each level card shows recommended age and board size', async ({ page }) => {
    await page.goto('/guest-page');

    const level1Card = page.getByRole('article', { name: /Nivel 1:/ });
    await expect(level1Card.getByLabel('Edad recomendada')).toBeVisible();
    await expect(level1Card.getByLabel('Tamaño del tablero')).toBeVisible();
  });

  test('Volver button navigates back to home', async ({ page }) => {
    await page.goto('/guest-page');
    await page.getByRole('button', { name: / Volver/ }).click();

    await expect(page).toHaveURL('/');
  });

  test('selecting Level 1 opens board with guest mode query params', async ({ page }) => {
    await page.goto('/guest-page');

    await page.getByLabel('Entrar al tablero de Nivel 1').getByRole('button', { name: 'Usar este nivel' }).click();

    await expect(page).toHaveURL(/\/board\/\d+\?mode=guest&level=LEVEL_1/);
  });

  test('guest Level 1 board has no phrase bar', async ({ page }) => {
    await page.goto('/guest-page');
    await page.getByLabel('Entrar al tablero de Nivel 1').getByRole('button', { name: 'Usar este nivel' }).click();
    await page.waitForURL(/\/board\//);

    await expect(page.getByRole('region', { name: 'Barra de construcción de frases' })).not.toBeVisible();
  });

  test('guest Level 1 board has no exit lock (uses back button instead)', async ({ page }) => {
    await page.goto('/guest-page');
    await page.getByLabel('Entrar al tablero de Nivel 1').getByRole('button', { name: 'Usar este nivel' }).click();
    await page.waitForURL(/\/board\//);

    await expect(page.getByRole('button', { name: 'Volver a la selección de nivel' })).toBeVisible();
    await expect(page.getByRole('button', { name: /Botón de salida/ })).not.toBeVisible();
  });

  test('guest Level 2 board shows phrase bar', async ({ page }) => {
    await page.goto('/guest-page');
    await page.getByLabel('Entrar al tablero de Nivel 2').getByRole('button', { name: 'Usar este nivel' }).click();
    await page.waitForURL(/\/board\//);

    await expect(page.getByRole('region', { name: 'Barra de construcción de frases' })).toBeVisible();
  });

  test('back button from guest board returns to guest-page', async ({ page }) => {
    await page.goto('/guest-page');
    await page.getByLabel('Entrar al tablero de Nivel 1').getByRole('button', { name: 'Usar este nivel' }).click();
    await page.waitForURL(/\/board\//);

    await page.getByRole('button', { name: 'Volver a la selección de nivel' }).click();

    await expect(page).toHaveURL('/guest-page');
  });

  test('pictograms load with ARASAAC images in guest mode', async ({ page }) => {
    await page.goto('/guest-page');
    await page.getByLabel('Entrar al tablero de Nivel 1').getByRole('button', { name: 'Usar este nivel' }).click();
    await page.waitForURL(/\/board\//);

    const firstPicto = page.getByRole('button', { name: /Pictograma .* Pulsa para escuchar/ }).first();
    await expect(firstPicto).toBeVisible();

    const img = firstPicto.getByRole('img');
    await expect(img).toBeVisible();
    const src = await img.getAttribute('src');
    expect(src).toMatch(/arasaac\.org|\/assets/);
  });
});
