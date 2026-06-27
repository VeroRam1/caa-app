import { test, expect } from '@playwright/test';

test.use({ storageState: 'e2e/auth.json' });

test.describe('My Boards (/my-boards)', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/my-boards');
  });

  test('page shows predefined boards and own boards sections', async ({ page }) => {
    await expect(page.getByRole('region', { name: 'Tableros predeterminados' })).toBeVisible();
    await expect(page.getByRole('region', { name: 'Mis tableros' })).toBeVisible();
  });

  test('predefined boards are grouped by category', async ({ page }) => {
    const predefined = page.getByRole('region', { name: 'Tableros predeterminados' });
    await expect(predefined.getByRole('heading', { name: 'General', level: 3 })).toBeVisible();
    await expect(predefined.getByRole('heading', { name: 'Alimentos', level: 3 })).toBeVisible();
    await expect(predefined.getByRole('heading', { name: 'Emociones', level: 3 })).toBeVisible();
  });

  test('each predefined board card has Ver and Copiar y editar buttons', async ({ page }) => {
    const firstCard = page.getByRole('article').first();
    await expect(firstCard.getByRole('button', { name: / Ver/ })).toBeVisible();
    await expect(firstCard.getByRole('button', { name: / Copiar y editar/ })).toBeVisible();
  });

  test('Volver al panel button navigates back to dashboard', async ({ page }) => {
    await page.getByRole('button', { name: 'Volver al panel de perfiles' }).click();
    await expect(page).toHaveURL('/dashboard');
  });

  test('Ver button on predefined board opens board in tutor mode', async ({ page }) => {
    const firstCard = page.getByRole('article').first();
    await firstCard.getByLabel(/Ver tablero/).getByRole('button', { name: / Ver/ }).click();

    await expect(page).toHaveURL(/\/board\/\d+\?mode=tutor/);
    await expect(page.getByRole('banner')).toContainText(/Tablero/);
  });

  test('Crear tablero nuevo button is visible in My Boards section', async ({ page }) => {
    const myBoards = page.getByRole('region', { name: 'Mis tableros' });
    await expect(myBoards.getByRole('button', { name: ' Crear tablero nuevo' })).toBeVisible();
  });

  test('copying a predefined board adds it to Mis tableros', async ({ page }) => {
    const myBoards = page.getByRole('region', { name: 'Mis tableros' });
    const predefined = page.getByRole('region', { name: 'Tableros predeterminados' });

    // Copy the first predefined board
    const firstCard = predefined.getByRole('article').first();
    const boardName = (await firstCard.getByRole('paragraph').first().textContent())!.trim();
    const escapedName = boardName.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    const countBefore = await myBoards.getByText(new RegExp(escapedName)).count();

    await firstCard.getByLabel(/Crear copia editable de/).getByRole('button', { name: / Copiar y editar/ }).click();

    // Copy navigates to the board editor — go back to verify the board was added
    await expect(page).toHaveURL(/\/board-editor\/\d+/, { timeout: 5000 });
    await page.goto('/my-boards');

    // The board count in Mis tableros should have increased by 1
    await expect(myBoards.getByText(new RegExp(escapedName))).toHaveCount(countBefore + 1, { timeout: 5000 });
  });
});
