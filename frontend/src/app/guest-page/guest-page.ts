import { Component, OnInit } from '@angular/core';
import { MenuModule } from 'primeng/menu';
import { ToolbarModule } from 'primeng/toolbar';
import { Board } from '../services/boardService';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { BoardService } from '../services/boardService';
import { ChangeDetectorRef } from '@angular/core';
import { Button, ButtonModule } from 'primeng/button';
import { Credits } from '../credits/credits';
import { ViewChild } from '@angular/core';


interface LevelCard {
  level: number;
  title: string;
  description: string;
  grid: string;         // e.g. "3 × 4"
  ageRange: string;
  features: string[];
  board: Board | null;  // the default board preview for this level
  boardId: number | null;
}

@Component({
  selector: 'app-guest-page',
  imports: [
    MenuModule,
    ToolbarModule,
    ButtonModule,
    Credits
  ],
  templateUrl: './guest-page.html',
  styleUrl: './guest-page.scss',
})
export class GuestPage implements OnInit {
  @ViewChild(Credits) creditsDialog!: Credits;

  menuItems: MenuItem[] = [];
  loading: boolean = true;

  levelCards: LevelCard[] = [
    {
      level: 1,
      title: 'Nivel 1',
      description: 'Tablero básico para primeros pasos en la comunicación. ' +
        'Pictogramas grandes con vocabulario esencial del día a día.',
      grid: '3 × 4',
      ageRange: '2 - 4 años',
      features: [
        '12 pictogramas grandes',
        'Vocabulario esencial',
        'Sin construcción de frases',
        'Síntesis de voz'
      ],
      board: null,
      boardId: null
    },
    {
      level: 2,
      title: 'Nivel 2',
      description: 'Tablero intermedio con más vocabulario y la posibilidad ' +
        'de construir frases sencillas combinando pictogramas.',
      grid: '4 × 5',
      ageRange: '4 - 7 años',
      features: [
        '20 pictogramas por tablero',
        'Construcción de frases',
        'Categorías ampliadas',
        'Síntesis de voz'
      ],
      board: null,
      boardId: null
    },
    {
      level: 3,
      title: 'Nivel 3',
      description: 'Tablero avanzado con vocabulario extenso para comunicación ' +
        'más compleja y frases elaboradas.',
      grid: '5 × 6',
      ageRange: '7+ años',
      features: [
        '30 picogramas por tablero',
        'Frases complejas',
        'Todas las categorías',
        'Síntesis de voz'
      ],
      board: null,
      boardId: null
    }
  ];

  constructor(
    private router: Router,
    private boardService: BoardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.initMenu();
    this.loadPreviewBoards();
  }

  initMenu(): void {
    this.menuItems = [
      { label: 'Créditos', icon: 'pi pi-info-circle', command: () => this.creditsDialog.show()},
      { label: 'Manual de usuario', icon: 'pi pi-book', command: () => {} }
    ];
  }

  onLevelSelect(card: LevelCard): void {
    if (!card.boardId) return;
    this.router.navigate(['/board', card.boardId], { 
      queryParams: { 
        mode: 'guest',
        level: 'LEVEL_' +card.level } });
  }

  onBack(): void {
    this.router.navigate(['/']);
  }

  // Builds a mini preview grid from the board pictograms.
  getPreviewGrid(board: Board): (string | null)[][] {
    const grid: (string | null)[][] = Array.from(
      { length: board.rows }, () => Array(board.columns).fill(null)
    );
    for (const p of board.pictograms) {
      if (p.positionY < board.rows && p.positionX < board.columns) {
        grid[p.positionY][p.positionX] = p.pictogramUrl;
      }
    }
    return grid;
  }

  loadPreviewBoards(): void {
    this.loading = true;
    this.cdr.markForCheck();

    this.boardService.getPredefinedBoards().subscribe({
      next: (boards) => {
        console.log('Tableros predeterminados:', boards.map(b => ({
        id: b.id,
        name: b.name,
        level: b.level,        // ← ver si llega
        isPredefined: b.isPredefined
      })));
        this.levelCards.forEach((card, index )=> {
          const level = card.level;
          const board = boards.find(b => 
            b.level === level &&
            (b.name.toLowerCase().includes('general') || 
            b.name.toLowerCase().includes('básico') || 
            b.name.toLowerCase().includes('basico'))
          );
          console.log(`Nivel ${level} → encontrado:`, board?.name, 'level:', board?.level);
          if (board) {
            this.boardService.getBoardById(board.id).subscribe({
              next: (fullBoard) => {
                this.levelCards[index].board = fullBoard;
                this.levelCards[index].boardId = fullBoard.id;
                this.cdr.markForCheck();
              }
            });
          } 
        });
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

}
