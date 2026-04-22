import { Component, OnInit } from '@angular/core';
import { MenuModule } from 'primeng/menu';
import { ToolbarModule } from 'primeng/toolbar';
import { Board } from '../services/boardService';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { BoardService } from '../services/boardService';
import { ChangeDetectorRef } from '@angular/core';
import { Button, ButtonModule } from 'primeng/button';


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
    ButtonModule
  ],
  templateUrl: './guest-page.html',
  styleUrl: './guest-page.scss',
})
export class GuestPage implements OnInit {

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
        'Más pictogramas',
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
        'Vocabulario amplio',
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
      { label: 'Sobre nosotros', icon: 'pi pi-info-circle', command: () => {} },
      { label: 'Manual de usuario', icon: 'pi pi-book', command: () => {} },
      { label: 'Tutoriales', icon: 'pi pi-play-circle', command: () => {} },
      { label: 'Contacto', icon: 'pi pi-envelope', command: () => {} }
    ];
  }

  loadPreviewBoards(): void {
  this.loading = true;
  this.cdr.markForCheck();

  this.boardService.getPredefinedBoardsByLevel(1).subscribe({
    next: (boards) => {
      // Busca por nombre flexible — "General" o "Básico"
      const general = boards.find(b =>
        b.name.toLowerCase().includes('general') ||
        b.name.toLowerCase().includes('básico') ||
        b.name.toLowerCase().includes('basico')
      );
      if (general) {
        // Carga con pictogramas para el preview
        this.boardService.getBoardById(general.id).subscribe({
          next: (fullBoard) => {
            this.levelCards[0].board = fullBoard;
            this.levelCards[0].boardId = fullBoard.id;
            this.loading = false;
            this.cdr.markForCheck();
          }
        });
      } else {
        this.loading = false;
        this.cdr.markForCheck();
      }
    },
    error: () => {
      this.loading = false;
      this.cdr.markForCheck();
    }
  });
}

  onLevelSelect(card: LevelCard): void {
    if (card.boardId) {
      this.router.navigate(['/board', card.boardId]);
    }
    // Levels 2 and 3 do nothing — not implemented yet
  }

  onBack(): void {
    this.router.navigate(['/']);
  }

  /**
   * Builds a mini preview grid from the board pictograms.
   * Returns a 2D array of pictogram URLs for the preview thumbnail.
   */
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

}
