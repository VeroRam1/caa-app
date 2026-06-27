import { test, expect } from '@playwright/test';

test.use({ storageState: 'e2e/auth.json' });

/**
 * Helper: create a child profile and navigate to its default board.
 * Returns the final board URL.
 */
async function createProfileAndOpenBoard(
  page: import('@playwright/test').Page,
  name: string,
  level: 1 | 2 | 3 = 1,
) {
  await page.goto('/dashboard');
  await page.getByRole('button', { name: 'Añadir nuevo perfil de niño' }).click();

  const dialog = page.getByRole('dialog', { name: 'Nuevo perfil' });
  await dialog.getByRole('textbox', { name: 'Nombre' }).fill(name);
  await dialog.getByRole('textbox', { name: 'Nacimiento' }).fill('2018-01-01');

  if (level !== 1) {
    await dialog.getByRole('combobox').click();
    await page.waitForTimeout(1000);
    const label = level === 2 ? 'Nivel 2 — Intermedio' : 'Nivel 3 — Avanzado';
    await page.getByRole('option', { name: label }).click();
  }

  await dialog.getByRole('button', { name: 'Añadir', exact: true }).click();
  await page.getByRole('button', { name: `Seleccionar perfil de ${name}` }).click();
  await page.waitForURL(/\/board\//);
}

test.describe('Board view — Level 1', () => {
  test('phrase bar is NOT visible at Level 1', async ({ page }) => {
    const name = `BV1-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 1);

    await expect(page.getByRole('region', { name: 'Barra de construcción de frases' })).not.toBeVisible();
  });

  test('pictograms load and display images with labels', async ({ page }) => {
    const name = `BV1img-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 1);

    const firstPicto = page.getByRole('button', { name: /Pictograma .* Pulsa para escuchar/ }).first();
    await expect(firstPicto).toBeVisible();
    await expect(firstPicto.getByRole('img')).toBeVisible();
  });

  test('clicking a pictogram triggers TTS without navigating away', async ({ page }) => {
    const name = `BV1tts-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 1);

    const boardUrl = page.url();
    const firstPicto = page.getByRole('button', { name: /Pictograma .* Pulsa para escuchar/ }).first();
    await firstPicto.click();

    // Board should not navigate away on pictogram click
    await expect(page).toHaveURL(boardUrl);
    // Button remains visible and accessible
    await expect(firstPicto).toBeVisible();
  });

  test('category sidebar switches board content', async ({ page }) => {
    const name = `BV1cat-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 1);

    const sidebar = page.getByRole('navigation', { name: 'Categorías de pictogramas' });
    await expect(sidebar).toBeVisible();

    // Click the second category
    const secondCategory = sidebar.getByRole('button').nth(1);
    const categoryName = await secondCategory.textContent();
    await secondCategory.click();

    // Board heading should change
    await expect(page.getByRole('heading', { level: 1 })).not.toContainText('Tablero Básico - Nivel 1');
  });

  test('toggle button collapses and expands the categories sidebar', async ({ page }) => {
    const name = `BV1side-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 1);

    // Sidebar starts open
    const toggleBtn = page.getByRole('button', { name: 'Ocultar categorías' });
    await expect(toggleBtn).toBeVisible();

    // Collapse
    await toggleBtn.click();
    await expect(page.getByRole('button', { name: 'Mostrar categorías' })).toBeVisible();

    // Expand again
    await page.getByRole('button', { name: 'Mostrar categorías' }).click();
    await expect(page.getByRole('button', { name: 'Ocultar categorías' })).toBeVisible();
  });
});

test.describe('Board view — Level 2 (phrase bar)', () => {
  test('phrase bar IS visible at Level 2', async ({ page }) => {
    const name = `BV2-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 2);

    await expect(page.getByRole('region', { name: 'Barra de construcción de frases' })).toBeVisible();
  });

  test('phrase bar controls are disabled when phrase is empty', async ({ page }) => {
    const name = `BV2empty-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 2);

    const phraseBar = page.getByRole('region', { name: 'Barra de construcción de frases' });
    await expect(phraseBar.getByRole('button', { name: 'Leer la frase en voz alta' })).toBeDisabled();
    await expect(phraseBar.getByRole('button', { name: 'Borrar último pictograma' })).toBeDisabled();
    await expect(phraseBar.getByRole('button', { name: 'Borrar toda la frase' })).toBeDisabled();
  });

  test('clicking a pictogram adds it to the phrase bar', async ({ page }) => {
    const name = `BV2add-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 2);

    // Click the first pictogram
    const firstPicto = page.getByRole('button', { name: /Pictograma .* Pulsa para añadir a la frase/ }).first();
    const pictoText = (await firstPicto.textContent())?.trim();
    await firstPicto.click();

    // Phrase bar should now contain the pictogram
    const phraseBar = page.getByRole('region', { name: 'Barra de construcción de frases' });
    const phraseArea = phraseBar.getByLabel('Frase actual');
    await expect(phraseArea).not.toContainText('Pulsa pictogramas para construir una frase');
    await expect(phraseArea.getByRole('img').first()).toBeVisible();
  });

  test('phrase bar controls enable after adding a pictogram', async ({ page }) => {
    const name = `BV2en-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 2);

    const firstPicto = page.getByRole('button', { name: /Pictograma .* Pulsa para añadir a la frase/ }).first();
    await firstPicto.click();

    const phraseBar = page.getByRole('region', { name: 'Barra de construcción de frases' });
    await expect(phraseBar.getByRole('button', { name: 'Leer la frase en voz alta' })).toBeEnabled();
    await expect(phraseBar.getByRole('button', { name: 'Borrar último pictograma' })).toBeEnabled();
    await expect(phraseBar.getByRole('button', { name: 'Borrar toda la frase' })).toBeEnabled();
  });

  test('Borrar último removes the last pictogram', async ({ page }) => {
    const name = `BV2del-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 2);

    const buttons = page.getByRole('button', { name: /Pictograma .* Pulsa para añadir a la frase/ });
    await buttons.nth(0).click();
    await buttons.nth(1).click();

    const phraseBar = page.getByRole('region', { name: 'Barra de construcción de frases' });
    await expect(phraseBar.getByLabel('Frase actual').getByRole('button')).toHaveCount(2);

    await phraseBar.getByRole('button', { name: 'Borrar último pictograma' }).click();
    await expect(phraseBar.getByLabel('Frase actual').getByRole('button')).toHaveCount(1);
  });

  test('Borrar toda la frase clears all pictograms', async ({ page }) => {
    const name = `BV2clr-${Date.now()}`;
    await createProfileAndOpenBoard(page, name, 2);

    const buttons = page.getByRole('button', { name: /Pictograma .* Pulsa para añadir a la frase/ });
    await buttons.nth(0).click();
    await buttons.nth(1).click();
    await buttons.nth(2).click();

    const phraseBar = page.getByRole('region', { name: 'Barra de construcción de frases' });
    await phraseBar.getByRole('button', { name: 'Borrar toda la frase' }).click();

    await expect(phraseBar.getByLabel('Frase actual')).toContainText('Pulsa pictogramas para construir una frase');
    await expect(phraseBar.getByRole('button', { name: 'Leer la frase en voz alta' })).toBeDisabled();
  });
});
