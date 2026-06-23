import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { SplitterModule } from 'primeng/splitter';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth-service';
import { Router } from '@angular/router';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';
import { Credits } from '../credits/credits';
import { ViewChild } from '@angular/core';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    ToolbarModule,
    SplitterModule,
    DialogModule,
    InputTextModule,
    MenuModule,
    Credits
  ],
  templateUrl: './home-page.html',
  styleUrl: './home-page.scss',
})
export class HomePage {
  @ViewChild(Credits) creditsDialog!: Credits;
  
  menuItems: MenuItem[] = [
    { label: 'Créditos', icon: 'pi pi-info-circle', command: () => this.creditsDialog.show()},
    { label: 'Manual de usuario', icon: 'pi pi-book', command: () => {} }
  ];

  //Dialolgs visibility
  visibleLogin: boolean = false;
  visibleRegister: boolean = false;

  // Form data
  loginEmail: string = '';
  loginPassword: string = '';
  loginError: string = '';
  registerName: string = '';
  registerEmail: string = '';
  registerPassword: string = '';
  registerConfirmPassword: string = '';
  registerError: string = '';
  passwordMismatch: boolean = false;

  // Loading for desabling buttons while waiting for response
  loading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef     
  ) {}

  showLoginDialog() {
    this.loginError = '';
    this.visibleLogin = true;
    this.cdr.markForCheck();
  }

  showRegisterDialog() {
    this.registerError = '';
    this.visibleRegister = true;
    this.cdr.markForCheck();
  }

  onLogin() {
    this.loading = true;
    this.loginError = '';
    this.cdr.markForCheck();

    this.authService.login(this.loginEmail, this.loginPassword).subscribe({
      next: (response) => {
        this.authService.saveToken(response.data.token);
        this.authService.saveTutorName(response.data.name);
        this.loading = false;
        this.visibleLogin = false;
        this.cdr.markForCheck();
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        // Shows backend message if exists
        this.loginError = err.error?.message || 'Email o contraseña incorrectos';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  onRegister() {
    if (this.registerPassword !== this.registerConfirmPassword) {
      this.passwordMismatch = true;
      this.cdr.markForCheck();
      return;
    }
    this.passwordMismatch = false;
    this.loading = true;
    this.registerError = '';
    this.authService.register(this.registerName, this.registerEmail, this.registerPassword).subscribe({
      next: (response) => {
        this.authService.saveToken(response.data.token);
        this.authService.saveTutorName(response.data.name);
        this.loading = false;
        this.visibleRegister = false;
        this.cdr.markForCheck();
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.registerError = err.error?.message || 'Error al registrar usuario';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });

  }

  onCancelLogin(): void {
  this.visibleLogin = false;
  this.loginEmail = '';
  this.loginPassword = '';
  this.loginError = '';
  this.cdr.markForCheck();
}

onCancelRegister(): void {
  this.visibleRegister = false;
  this.registerName = '';
  this.registerEmail = '';
  this.registerPassword = '';
  this.registerConfirmPassword = '';
  this.registerError = '';
  this.passwordMismatch = false;
  this.cdr.markForCheck();
}

  onGuest() {
    this.router.navigate(['/guest']);
  }
}