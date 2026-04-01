import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { SplitterModule } from 'primeng/splitter';
import { PanelModule } from 'primeng/panel';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    CommonModule,
    ButtonModule,
    ToolbarModule,
    SplitterModule,
    DialogModule,
    InputTextModule
],
  
  templateUrl: './home-page.html',
  styleUrl: './home-page.scss',
})
export class HomePage {
  onLogin() {
    console.log("Ir a login");
  }

  onGuest() {
    console.log("Entrar sin registrarse");
  }

}
