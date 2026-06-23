import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-error-page',
  imports: [CommonModule, ButtonModule],
  templateUrl: './error-page.html',
  styleUrl: './error-page.scss',
})
export class ErrorPage {
  constructor(private router: Router) {}

  goHome(): void {
    this.router.navigate(['/']);
  }
}
