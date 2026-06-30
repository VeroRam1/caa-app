import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-user-manual',
  imports: [CommonModule, DialogModule, ButtonModule],
  templateUrl: './user-manual.html',
  styleUrl: './user-manual.scss',
})
export class UserManual {
  visible: boolean = false;
  show(): void { this.visible = true; }
  hide(): void { this.visible = false; }
}
