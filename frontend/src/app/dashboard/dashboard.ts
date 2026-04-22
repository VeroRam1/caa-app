import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { MenuModule } from 'primeng/menu';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { MenuItem } from 'primeng/api';
import { AuthService } from '../services/auth-service';
import { ChildProfileService, ChildProfile, CreateChildProfileRequest } from '../services/childProfile-service';
import { ConfirmPopupModule } from 'primeng/confirmpopup';   
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush, 
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    ToolbarModule,
    MenuModule,
    DialogModule,
    InputTextModule,
    SelectModule, 
    ButtonModule, 
    ConfirmPopupModule,
    ToastModule

  ],
  // ConfirmationService y MessageService needed for the confirmation pop up and the toast messages, respectively
  providers: [ConfirmationService, MessageService],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {

  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);

  tutorName: string = '';
  menuItems: MenuItem[] = [];
  childProfiles: ChildProfile[] = [];

  selectedFile: File | null = null;
  photoPreviewUrl: string | null = null;

  visibleAddProfile: boolean = false;
  visibleEditProfile: boolean = false;
  visibleEditActions: boolean = false; // Choose action on a profile (edit, delete, view)
  
  loading: boolean = false;
  loadingProfiles: boolean = false;
  
  editMode: boolean = false;

  // Select profile for editing or deleting. 
  selectedProfile: ChildProfile | null = null;

  //Add profile form fields
  newProfileName: string = '';
  newProfileBirthDate: string = '';
  newProfilePhoto: string = '';
  newProfileLevel: string = 'LEVEL_1';
  newSelectedFile: File | null = null;
  newPhotoPreviewUrl: string | null = null;

  //Edit profile form fields
  editProfileName: string = '';
  editProfileBirthDate: string = '';
  editProfilePhoto: string = '';
  editProfileLevel: string = 'LEVEL_1';

  levelOptions = [
    { label: 'Nivel 1 — Básico', value: 'LEVEL_1' },
    { label: 'Nivel 2 — Intermedio', value: 'LEVEL_2' },
    { label: 'Nivel 3 — Avanzado', value: 'LEVEL_3' }
  ];

  constructor(
    private authService: AuthService,
    public childProfileService: ChildProfileService,
    private router: Router,
    private cdr: ChangeDetectorRef 
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    }
    this.tutorName = this.authService.getTutorName();
    this.initMenu();
    this.loadProfiles();
  }

  initMenu(): void {
    this.menuItems = [
      { label: 'Sobre nosotros', icon: 'pi pi-info-circle', command: () => {} },
      { label: 'Manual de usuario', icon: 'pi pi-book', command: () => {} },
      { label: 'Tutoriales', icon: 'pi pi-play-circle', command: () => {} },
      { label: 'Contacto', icon: 'pi pi-envelope', command: () => {} },
      { separator: true },
      { label: 'Configuración', icon: 'pi pi-cog', command: () => {} }
    ];
  }

  loadProfiles(): void {
    this.loadingProfiles = true;
    this.cdr.markForCheck(); 
    
    this.childProfileService.getAllChildProfiles().subscribe({
      next: (profiles) => {
        this.childProfiles = profiles;
        this.loadingProfiles = false;
        this.cdr.markForCheck(); 
      },
      error: (err) => {
        console.error('Error cargando perfiles:', err);
        this.loadingProfiles = false;
        this.cdr.markForCheck();
      }
    });
  }

  // Edit mode
  toggleEditMode(): void{
    this.editMode = !this.editMode;
    this.cdr.markForCheck();
  }

  // When clicking on a profile opens the actions dialog
  onProfileClick(profile: ChildProfile): void {
    if (this.editMode) {
      this.openActionsDialog(profile);
    } else {
      this.onSelectProfile(profile);
    }
  }

  onNewFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];

    if (!file.type.startsWith('image/')) {
      this.messageService.add({
        severity: 'error',
        summary: 'Archivo no válido',
        detail: 'Solo se permiten imágenes (JPG, PNG, GIF, WebP)'
      });
      return;
    }

    if (file.size > 5 * 1024 * 1024) {
      this.messageService.add({
        severity: 'error',
        summary: 'Archivo demasiado grande',
        detail: 'La imagen no puede superar los 5 MB'
      });
      return;
    }

    this.newSelectedFile = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      this.newPhotoPreviewUrl = e.target?.result as string;
      this.cdr.markForCheck();
    };
    reader.readAsDataURL(file);
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];

    // Validate file type in frontend too — criteria 3.3.1
    if (!file.type.startsWith('image/')) {
      this.messageService.add({
        severity: 'error',
        summary: 'Archivo no válido',
        detail: 'Solo se permiten imágenes (JPG, PNG, GIF, WebP)'
      });
      return;
    }

    // Validate file size — max 5MB
    if (file.size > 5 * 1024 * 1024) {
      this.messageService.add({
        severity: 'error',
        summary: 'Archivo demasiado grande',
        detail: 'La imagen no puede superar los 5 MB'
      });
      return;
    }

    this.selectedFile = file;

    // Show local preview immediately — no need to wait for upload
    const reader = new FileReader();
    reader.onload = (e) => {
      this.photoPreviewUrl = e.target?.result as string;
      this.cdr.markForCheck();
    };
    reader.readAsDataURL(file);
  }

  onSelectProfile(profile: ChildProfile): void {
      // Goes to the first assigned board of the profile, if it has any. Otherwise, goes to the default board (id=1)
      if (profile.assignedBoards && profile.assignedBoards.length > 0) { // TODO: Si no tine tableros asignados, debería ver el nivel e ir al tablero predeterminado de ese nivel
        this.router.navigate(['/board', profile.assignedBoards[0].id]);
      } else {
        // If it has no assigned boards, navigate to the default board (id=1)
        this.router.navigate(['/board', 1]);
      }
    }

  // Actions dialog
  openActionsDialog(profile: ChildProfile): void {
    this.selectedProfile = profile;
    this.visibleEditActions = true;
    this.cdr.markForCheck();
  }

  onEditSelected(): void {
    if (!this.selectedProfile) return;
    // Fill the edit form with the profile data
    this.editProfileName = this.selectedProfile.name;
    this.editProfileBirthDate = this.selectedProfile.birthDate || '';
    this.editProfilePhoto = this.selectedProfile.photoUrl || '';
    this.editProfileLevel = this.selectedProfile.level;
    this.selectedFile = null; 
    this.photoPreviewUrl = this.selectedProfile.photoUrl;
    this.visibleEditActions = false;
    this.visibleEditProfile = true;
    this.cdr.markForCheck();
  }

  confirmDeleteProfile(event: Event): void {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: `¿Seguro que quieres eliminar el perfil de ${this.selectedProfile?.name}? Esta acción no se puede deshacer.`,
      accept: () => {
        this.deleteProfile();
      }
    });
  }

  deleteProfile(): void {
    if (!this.selectedProfile) return;
    this.loading = true;
    this.cdr.markForCheck();

    this.childProfileService.deleteProfile(this.selectedProfile.id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Perfil eliminado',
          detail: `El perfil de ${this.selectedProfile?.name} ha sido eliminado.`
        });
        this.visibleEditActions = false;
        this.selectedProfile = null;
        this.loading = false;
        this.cdr.markForCheck();
        setTimeout(() => this.loadProfiles(), 0);
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo eliminar el perfil. Inténtalo de nuevo.'
        });
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  // Edit profile
  onUpdateProfile(): void {
    if (!this.selectedProfile || !this.editProfileName.trim()) return;
    this.loading = true;
    this.cdr.markForCheck();

    const request = {
      name: this.editProfileName,
      birthDate: this.editProfileBirthDate || null,
      photoUrl: this.editProfilePhoto || null,
      level: this.editProfileLevel
    };

    // First update profile data, then upload photo if one was selected
    this.childProfileService.updateProfile(this.selectedProfile.id, request).subscribe({
      next: () => {
        if (this.selectedFile && this.selectedProfile) {
          // Upload photo after updating profile data
          this.childProfileService.uploadPhoto(
            this.selectedProfile.id, this.selectedFile
          ).subscribe({
            next: () => {
              this.finishUpdate();
            },
            error: () => {
              // Profile was updated but photo failed — show partial success
              this.messageService.add({
                severity: 'warn',
                summary: 'Perfil actualizado',
                detail: 'Los datos se guardaron pero no se pudo subir la foto.'
              });
              this.finishUpdate();
            }
          });
        } else {
          this.finishUpdate();
        }
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo actualizar el perfil. Inténtalo de nuevo.'
        });
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  private finishUpdate(): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Perfil actualizado',
      detail: `Los datos de ${this.editProfileName} han sido guardados.`
    });
    this.visibleEditProfile = false;
    this.selectedProfile = null;
    this.selectedFile = null;
    this.photoPreviewUrl = null;
    this.loading = false;
    this.cdr.markForCheck();
    setTimeout(() => this.loadProfiles(), 0);
  }

  showAddProfileDialog(): void {
    this.newProfileName = '';
    this.newProfileBirthDate = '';
    this.newProfilePhoto = '';
    this.newSelectedFile = null;
    this.newPhotoPreviewUrl = null;
    this.newProfileLevel = 'LEVEL_1';
    this.visibleAddProfile = true;
    this.cdr.markForCheck();
  }

  onAddProfile(): void {
    if (!this.newProfileName.trim()) return;
    this.loading = true;
    this.cdr.markForCheck();

    const request: CreateChildProfileRequest = {
      name: this.newProfileName,
      birthDate: this.newProfileBirthDate || null,
      photoUrl: null,
      level: this.newProfileLevel
    };

    this.childProfileService.createProfile(request).subscribe({
      next: (response) => {
        const newProfileId = response.data?.id;

        if (this.newSelectedFile && newProfileId) {
          this.childProfileService.uploadPhoto(newProfileId, this.newSelectedFile).subscribe({
            next: () => {
              this.finishAddProfile();
            },
            error: () => {
              // Profile created but photo uploading failed
              this.messageService.add({
                severity: 'warn',
                summary: 'Perfil creado',
                detail: 'El perfil fue creado pero no se pudo subir la foto.'
              });
              this.finishAddProfile();
            }
          });
        } else {
          this.finishAddProfile();
        }
      },
      error: () => {
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  private finishAddProfile(): void {
    this.loading = false;
    this.visibleAddProfile = false;
    this.newSelectedFile = null;
    this.newPhotoPreviewUrl = null;
    this.cdr.markForCheck();
    setTimeout(() => this.loadProfiles(), 0);
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  // Shows a confirmation pop up before logging out
  confirmLogout(event: Event): void {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: '¿Seguro que quieres cerrar la sesión?',
      accept: () => {
        this.authService.logout();
        this.router.navigate(['/']);
      }
    });
  }

  getInitials(name: string): string {
    return name.trim().charAt(0).toUpperCase();
  }

  getLevelLabel(level: string): string {
    const labels: Record<string, string> = {
      'LEVEL_1': 'Nivel 1',
      'LEVEL_2': 'Nivel 2',
      'LEVEL_3': 'Nivel 3'
    };
    return labels[level] || level;
  }

  
}