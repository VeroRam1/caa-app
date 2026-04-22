import { Component, inject, ChangeDetectionStrategy, OnInit } from '@angular/core';
import { Board, BoardPictogram, BoardService } from '../services/boardService';
import { MenuItem } from 'primeng/api';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { MenuModule } from 'primeng/menu';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ToastModule } from 'primeng/toast';

// Category definition — links a display name to a board name in the backend
interface Category {
  label: string;
  icon: string;
  boardName: string;
}

@Component({
  selector: 'app-board-view',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ConfirmationService, MessageService],
  imports: [
    CommonModule,
    ButtonModule,
    ToolbarModule,
    MenuModule,
    ConfirmPopupModule,
    ToastModule
  ],
  templateUrl: './board-view.html',
  styleUrl: './board-view.scss',
})
export class BoardView implements OnInit {
  private confirmationService = inject(ConfirmationService);

  board: Board | null = null;
  allBoards: Board[] = [];        // all level-1 predefined boards
  loading: boolean = true;
  loadingCategory: boolean = false;
  error: string = '';
  grid: (BoardPictogram | null)[][] = [];

  // Sidebar state
  sidebarOpen: boolean = true;
  activeCategory: string = 'Básico';

  // Categories matching the board names created by DataInitializer
  categories: Category[] = [
    { label: 'Básico',    icon: '🏠', boardName: 'Tablero Básico - Nivel 1' },
    { label: 'Alimentos',  icon: '🍎', boardName: 'Alimentos - Nivel 1' },
    { label: 'Emociones',  icon: '😊', boardName: 'Emociones - Nivel 1' },
    { label: 'Lugares',    icon: '🏠', boardName: 'Lugares - Nivel 1' },
    { label: 'Personas',   icon: '👨‍👩‍👧', boardName: 'Personas - Nivel 1' },
  ];

  // Lock mechanism
  lockTapCount: number = 0;
  lockTapTimer: ReturnType<typeof setTimeout> | null = null;
  lockShaking: boolean = false;

  private synth = window.speechSynthesis;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private boardService: BoardService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Load all predefined boards first, then show the requested one
    this.loadAllBoards();
  }

  loadAllBoards(): void {
    this.loading = true;
    this.cdr.markForCheck();

    this.boardService.getPredefinedBoardsByLevel(1).subscribe({
      next: (boards) => {
        this.allBoards = boards;

        // Try to load the board requested via route param with full pictogram data
        const boardId = Number(this.route.snapshot.paramMap.get('id'));
        const requested = boards.find(b => b.id === boardId);
        const target = requested || boards.find(b => b.name.includes('Básico')) || boards[0];

        if (target) {
          this.loadBoardWithPictograms(target.id);
        } else {
          this.error = 'No hay tableros disponibles.';
          this.loading = false;
          this.cdr.markForCheck();
        }
      },
      error: () => {
        this.error = 'Error al cargar los tableros.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  
  // Loads a specific board with all its pictograms via GET /api/boards/{id}
  loadBoardWithPictograms(id: number): void {
    this.loading = true;
    this.cdr.markForCheck();

    this.boardService.getBoardById(id).subscribe({
      next: (board) => {
        this.showBoard(board);
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'Error al cargar el tablero.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  onCategorySelect(category: Category): void {
    const target = this.allBoards.find(b => b.name === category.boardName);
    console.log('target:', target?.id, target?.name);
    console.log('board actual:', this.board?.id, this.board?.name);
    console.log('activeCategory:', this.activeCategory);
    
    if (!target) return;
    if (this.board?.id === target.id) return;

    this.activeCategory = category.label;
    this.loadBoardWithPictograms(target.id);
    this.router.navigate(['/board', target.id], { replaceUrl: true });
  }
  showBoard(board: Board): void {
    this.board = board;
    this.buildGrid(board);
    // Set active category based on board name
    const cat = this.categories.find(c => c.boardName === board.name);
    if (cat) this.activeCategory = cat.label;
    this.cdr.markForCheck();
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
    this.cdr.markForCheck();
  }

  buildGrid(board: Board): void {
    this.grid = Array.from({ length: board.rows }, () =>
      Array(board.columns).fill(null)
    );
    for (const pictogram of board.pictograms) {
      const row = pictogram.positionY;
      const col = pictogram.positionX;
      if (row < board.rows && col < board.columns) {
        this.grid[row][col] = pictogram;
      }
    }
  }

  speakPictogram(pictogram: BoardPictogram): void {
    if (!this.synth) return;
    this.synth.cancel();
    const utterance = new SpeechSynthesisUtterance(pictogram.text);
    utterance.lang = 'es-ES';
    utterance.rate = 0.85;
    utterance.pitch = 1.1;
    utterance.volume = 1;
    this.synth.speak(utterance);
  }

  onLockTap(event: Event): void {
    this.lockTapCount++;
    this.lockShaking = true;
    this.cdr.markForCheck();
    setTimeout(() => {
      this.lockShaking = false;
      this.cdr.markForCheck();
    }, 400);

    if (this.lockTapCount === 1) {
      this.lockTapTimer = setTimeout(() => {
        this.lockTapCount = 0;
        this.cdr.markForCheck();
      }, 3000);
    }

    if (this.lockTapCount >= 3) {
      if (this.lockTapTimer) {
        clearTimeout(this.lockTapTimer);
        this.lockTapTimer = null;
      }
      this.lockTapCount = 0;
      this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: '¿Quieres salir del tablero?',
        accept: () => {
          this.authService.isLoggedIn()
            ? this.router.navigate(['/dashboard'])
            : this.router.navigate(['/guest']);
        }
      });
      this.cdr.markForCheck();
    }
  }

  trackByRow(index: number): number { return index; }
  trackByCol(index: number, cell: BoardPictogram | null): number {
    return cell ? cell.id : index;
  }
}
