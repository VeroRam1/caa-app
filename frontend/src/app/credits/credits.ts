import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-credits',
  imports: [CommonModule, DialogModule, ButtonModule],
  templateUrl: './credits.html',
  styleUrl: './credits.scss',
})
export class Credits {
  visible: boolean = false;

  show(){
    this.visible = true;
  }

  hide(){
    this.visible = false;
  }

}
