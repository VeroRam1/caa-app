import { Component } from '@angular/core';
import { CdkDragDrop, CdkDrag, CdkDropList, moveItemInArray } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-board-editor',
  imports: [CdkDrag, CdkDropList],
  templateUrl: './board-editor.html',
  styleUrl: './board-editor.scss',
})
export class BoardEditor {

}
