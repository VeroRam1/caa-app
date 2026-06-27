import { test, expect } from '@playwright/test';

test.use({ storageState: 'e2e/auth.json' });

test.describe('Dashboard — child profiles', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/dashboard');
  });

  test('shows welcome heading and add-profile button', async ({ page }) => {
    await expect(page.getByRole('heading', { name: /Bienvenido/ })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Añadir nuevo perfil de niño' })).toBeVisible();
  });

  test('toolbar has Mis tableros, Editar perfiles and Cerrar sesión buttons', async ({ page }) => {
    await expect(page.getByRole('button', { name: ' Mis tableros' })).toBeVisible();
    await expect(page.getByRole('button', { name: ' Editar perfiles' })).toBeVisible();
    await expect(page.getByRole('button', { name: ' Cerrar sesión' })).toBeVisible();
  });

  test('can create a Level 1 child profile', async ({ page }) => {
    const name = `L1-${Date.now()}`;

    await page.getByRole('button', { name: 'Añadir nuevo perfil de niño' }).click();

    const dialog = page.getByRole('dialog', { name: 'Nuevo perfil' });
    await expect(dialog).toBeVisible();

    await dialog.getByRole('textbox', { name: 'Nombre' }).fill(name);
    await dialog.getByRole('textbox', { name: 'Nacimiento' }).fill('2018-06-15');
    await dialog.getByRole('button', { name: 'Añadir', exact: true }).click();

    await expect(page.getByRole('button', { name: `Seleccionar perfil de ${name}` })).toBeVisible();
  });

  test('can create a Level 2 child profile', async ({ page }) => {
    const name = `L2-${Date.now()}`;

    await page.getByRole('button', { name: 'Añadir nuevo perfil de niño' }).click();

    const dialog = page.getByRole('dialog', { name: 'Nuevo perfil' });
    await dialog.getByRole('textbox', { name: 'Nombre' }).fill(name);
    await dialog.getByRole('textbox', { name: 'Nacimiento' }).fill('2016-03-01');

    // Open level dropdown and select Level 2
    await dialog.getByRole('combobox').click();
    await page.getByRole('option', { name: 'Nivel 2 — Intermedio' }).click();

    await dialog.getByRole('button', { name: 'Añadir', exact: true }).click();

    const profileCard = page.getByRole('button', { name: `Seleccionar perfil de ${name}` });
    await expect(profileCard).toBeVisible();
    await expect(profileCard.getByText('Nivel 2')).toBeVisible();
  });

  test('selecting a profile navigates to board view', async ({ page }) => {
    const name = `Nav-${Date.now()}`;

    // Create a profile
    await page.getByRole('button', { name: 'Añadir nuevo perfil de niño' }).click();
    const dialog = page.getByRole('dialog', { name: 'Nuevo perfil' });
    await dialog.getByRole('textbox', { name: 'Nombre' }).fill(name);
    await dialog.getByRole('textbox', { name: 'Nacimiento' }).fill('2019-01-01');
    await dialog.getByRole('button', { name: 'Añadir', exact: true }).click();

    // Select the profile
    await page.getByRole('button', { name: `Seleccionar perfil de ${name}` }).click();

    await expect(page).toHaveURL(/\/board\//);
  });

  test('edit mode shows edit buttons on profiles', async ({ page }) => {
    // Need at least one profile
    const name = `Edit-${Date.now()}`;
    await page.getByRole('button', { name: 'Añadir nuevo perfil de niño' }).click();
    const dialog = page.getByRole('dialog', { name: 'Nuevo perfil' });
    await dialog.getByRole('textbox', { name: 'Nombre' }).fill(name);
    await dialog.getByRole('textbox', { name: 'Nacimiento' }).fill('2019-01-01');
    await dialog.getByRole('button', { name: 'Añadir', exact: true }).click();

    // Activate edit mode
    await page.getByRole('button', { name: ' Editar perfiles' }).click();

    await expect(page.getByRole('status')).toContainText('Modo edición activo');
    await expect(page.getByRole('button', { name: `Editar perfil de ${name}` })).toBeVisible();
  });

  test('can edit a profile name', async ({ page }) => {
    const originalName = `Original-${Date.now()}`;
    const updatedName = `Updated-${Date.now()}`;

    // Create profile
    await page.getByRole('button', { name: 'Añadir nuevo perfil de niño' }).click();
    let dialog = page.getByRole('dialog', { name: 'Nuevo perfil' });
    await dialog.getByRole('textbox', { name: 'Nombre' }).fill(originalName);
    await dialog.getByRole('textbox', { name: 'Nacimiento' }).fill('2019-01-01');
    await dialog.getByRole('button', { name: 'Añadir', exact: true }).click();
    await expect(page.getByRole('button', { name: `Seleccionar perfil de ${originalName}` })).toBeVisible();

    // Enter edit mode and open actions dialog
    await page.getByRole('button', { name: ' Editar perfiles' }).click();
    await page.getByRole('button', { name: `Editar perfil de ${originalName}` }).click();

    // Open edit details panel
    const actionsDialog = page.getByRole('dialog', { name: `Perfil de ${originalName}` });
    await actionsDialog.getByLabel('Editar nombre, fecha de nacimiento y nivel del perfil').click();

    // Edit name in the edit dialog
    dialog = page.getByRole('dialog', { name: 'Editar perfil' });
    await expect(dialog).toBeVisible();
    const nameField = dialog.getByRole('textbox', { name: 'Nombre' });
    await nameField.clear();
    await nameField.fill(updatedName);
    await dialog.getByRole('button', { name: 'Guardar cambios' }).click();

    await expect(page.getByRole('button', { name: `Editar perfil de ${updatedName}` })).toBeVisible();
  });

  test('can delete a child profile', async ({ page }) => {
    const name = `Delete-${Date.now()}`;

    // Create profile
    await page.getByRole('button', { name: 'Añadir nuevo perfil de niño' }).click();
    const dialog = page.getByRole('dialog', { name: 'Nuevo perfil' });
    await dialog.getByRole('textbox', { name: 'Nombre' }).fill(name);
    await dialog.getByRole('textbox', { name: 'Nacimiento' }).fill('2019-01-01');
    await dialog.getByRole('button', { name: 'Añadir', exact: true }).click();
    await expect(page.getByRole('button', { name: `Seleccionar perfil de ${name}` })).toBeVisible();

    // Enter edit mode and open profile actions
    await page.getByRole('button', { name: ' Editar perfiles' }).click();
    await page.getByRole('button', { name: `Editar perfil de ${name}` }).click();

    // Click delete
    const actionsDialog = page.getByRole('dialog', { name: `Perfil de ${name}` });
    await actionsDialog.getByLabel('Eliminar este perfil de forma permanente').click();

    // Confirm in the PrimeNG ConfirmPopup (custom label "Confirmar")
    await page.getByRole('button', { name: 'Confirmar' }).click();

    await expect(page.getByRole('button', { name: `Seleccionar perfil de ${name}` })).not.toBeVisible();
    await expect(page.getByRole('button', { name: `Editar perfil de ${name}` })).not.toBeVisible();
  });
});
