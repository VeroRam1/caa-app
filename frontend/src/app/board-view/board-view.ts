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
import { forkJoin } from 'rxjs';

// Category definition — links a display name to a board name in the backend
interface SubLevel {
  label: string;
  level: number;
  boardName: string;
}

interface Category {
  label: string;
  iconPictogramId: number;
  subLevels: SubLevel[];
}
interface PhraseItem {
  pictogramUrl: string;
  text: string;
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
  allBoards: Board[] = [];
  loading: boolean = true;
  error: string = '';
  grid: (BoardPictogram | null)[][] = [];

  profileLevel: string = 'LEVEL_1';
  phraseItems: PhraseItem[] = [];

  // Sidebar state
  sidebarOpen: boolean = true;
  activeCategory: string = 'General';

  // Lock
  lockTapCount: number = 0;
  lockTapTimer: ReturnType<typeof setTimeout> | null = null;
  lockShaking: boolean = false;

  isTutorMode: boolean = false;
  isGuestMode: boolean = false;

  private synth = window.speechSynthesis;

  readonly allCategories: Category[] = [
    {
      label: 'General',
      iconPictogramId: 2,      
      subLevels: [
        { label: 'Nivel 1', level: 1, boardName: 'Tablero Básico - Nivel 1' },
        { label: 'Nivel 2', level: 2, boardName: 'Tablero General - Nivel 2' },
        { label: 'Nivel 3', level: 3, boardName: 'Tablero General - Nivel 3' }
      ]
    },
    {
      label: 'Alimentos',
      iconPictogramId: 7841,    
      subLevels: [
        { label: 'Nivel 1', level: 1, boardName: 'Alimentos - Nivel 1' },
        { label: 'Nivel 2', level: 2, boardName: 'Alimentos - Nivel 2' },
        { label: 'Nivel 3', level: 3, boardName: 'Alimentos - Nivel 3' }
      ]
    },
    {
      label: 'Emociones',
      iconPictogramId: 8484,  
      subLevels: [
        { label: 'Nivel 1', level: 1, boardName: 'Emociones - Nivel 1' },
        { label: 'Nivel 2', level: 2, boardName: 'Emociones - Nivel 2' },
        { label: 'Nivel 3', level: 3, boardName: 'Emociones - Nivel 3' }
      ]
    },
    {
      label: 'Lugares',
      iconPictogramId: 6038,   
      subLevels: [
        { label: 'Nivel 1', level: 1, boardName: 'Lugares - Nivel 1' }
      ]
    },
    {
      label: 'Personas',
      iconPictogramId: 7873,   
      subLevels: [
        { label: 'Nivel 1', level: 1, boardName: 'Personas - Nivel 1' }
      ]
    },
    {
      label: 'Acciones',
      iconPictogramId: 4886,   
      subLevels: [
        { label: 'Nivel 2', level: 2, boardName: 'Acciones - Nivel 2' },
        { label: 'Nivel 3', level: 3, boardName: 'Acciones - Nivel 3' }
      ]
    }
  ];

  get categories(): Category[] {
    const maxLevel = this.profileLevel === 'LEVEL_3' ? 3: this.profileLevel === 'LEVEL_2' ? 2 : 1;
    return this.allCategories
      .map(cat => ({
          ...cat, 
          subLevels: cat.subLevels.filter(sl => sl.level <= maxLevel)
    }))
    .filter(cat => cat.subLevels.length > 0);
  }

  get hasPhraseBar(): boolean {
    if (this.isTutorMode) return false;
    return this.profileLevel === 'LEVEL_2' || this.profileLevel === 'LEVEL_3';
  }

  getArasaacIconUrl(pictogramId: number): string {
    return `https://static.arasaac.org/pictograms/${pictogramId}/${pictogramId}_300.png`;
  }

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private boardService: BoardService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.isTutorMode = this.route.snapshot.queryParams['mode'] === 'tutor';
    this.isGuestMode = this.route.snapshot.queryParams['mode'] === 'guest';
    // Level from query param or localstorage (authenticated profile)
    const levelFromParam = this.route.snapshot.queryParams['level'];
    this.profileLevel = levelFromParam || this.authService.getActiveProfileLevel();
    console.log('Profile level:', this.profileLevel);
    this.loadAllBoards();
  }

  loadAllBoards(): void {
    this.loading = true;
    this.cdr.markForCheck();

    if (this.isTutorMode){
      const boardId = Number(this.route.snapshot.paramMap.get('id'));
      if (boardId > 0) {
        this.loadBoardWithPictograms(boardId);
      } else {
        this.error = 'Tablero no encontrado.';
        this.loading = false;
        this.cdr.markForCheck();
      }
      return;
    }

    const maxLevel = this.profileLevel === 'LEVEL_3' ? 3 :
                   this.profileLevel === 'LEVEL_2' ? 2 : 1;

    const levelRequests = Array.from({ length: maxLevel }, (_, i) =>
      this.boardService.getPredefinedBoardsByLevel(i + 1)
    );

    forkJoin(levelRequests).subscribe({
      next: (results) => {
        this.allBoards = results.flat();

        const boardId = Number(this.route.snapshot.paramMap.get('id'));
        const requested = boardId > 0
          ? this.allBoards.find(b => b.id === boardId)
          : null;

        const generalName = maxLevel === 1 ? 'Tablero Básico - Nivel 1' :
                            maxLevel === 2 ? 'Tablero General - Nivel 2' :
                                            'Tablero General - Nivel 3';

        const target = requested ||
          this.allBoards.find(b => b.name === generalName) ||
          this.allBoards[0];

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

  onBackToMyBoards(): void {
    this.router.navigate(['/my-boards']);
  }

  onBackToGuest(): void {
    this.router.navigate(['/guest-page']);
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

  showBoard(board: Board): void {
    this.board = board;
    this.buildGrid(board);
    // Find which category this board belongs to
    for (const cat of this.allCategories) {
      if (cat.subLevels.some(sl => sl.boardName === board.name)) {
        this.activeCategory = cat.label;
        break;
      }
    }
    this.cdr.markForCheck();
  }

  buildGrid(board: Board): void {
    this.grid = Array.from({ length: board.rows }, () =>
      Array(board.columns).fill(null)
    );
    for (const p of board.pictograms) {
      if (p.positionY < board.rows && p.positionX < board.columns) {
        this.grid[p.positionY][p.positionX] = p;
      }
    }
  }

  /** Category selection */
  // Called when a category button is clicked. If the category has only one sub-level, load it directly and if it has multiple, show the popup to choose.
  onCategoryClick(event: Event, category: Category, overlayPanel: any): void {
    const maxLevel = this.profileLevel === 'LEVEL_3' ? 3 :
                      this.profileLevel === 'LEVEL_2' ? 2 : 1;

    const subLevel = [...category.subLevels]
      .reverse()
      .find(sl => sl.level <= maxLevel);  
    if (!subLevel) return;

    const target = this.allBoards.find(b => b.name === subLevel.boardName);
    if (!target || this.board?.id === target.id) return;
   
    this.activeCategory = category.label;
    this.loadBoardWithPictograms(target.id);
    this.router.navigate(['/board', target.id], {replaceUrl: true});
    this.cdr.markForCheck();
    
  }

  onSubLevelSelect(subLevel: SubLevel, overlayPanel: any): void {
    const target = this.allBoards.find(b => b.name === subLevel.boardName);
    if (!target || this.board?.id === target.id) {
      overlayPanel?.hide();
      return;
    }
    this.loadBoardWithPictograms(target.id);
    this.router.navigate(['/board', target.id], {replaceUrl: true});
    overlayPanel?.hide();
    this.cdr.markForCheck();
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
    this.cdr.markForCheck();
  }

  /** Pictogram interaction */
  onPictogramClick(pictogram: BoardPictogram): void {
    this.speakText(pictogram.text);
    if (this.hasPhraseBar) {
      this.phraseItems.push({
        pictogramUrl: pictogram.pictogramUrl,
        text: pictogram.text
      });
      this.cdr.markForCheck();
    }
  }

  speakText(text: string): void {
    if (!this.synth) return;
    this.synth.cancel();
    const utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = 'es-ES';
    utterance.rate = 0.85;
    utterance.pitch = 1.1;
    utterance.volume = 1;
    this.synth.speak(utterance);
  }

  /** Phrase bar */

  speakPhrase(): void {
    if (this.phraseItems.length === 0) return;
    const phrase = this.phraseItems.map(p => p.text).join(' ');
    this.speakText(phrase);
  }

  removePhraseItem(index: number): void {
    this.phraseItems.splice(index, 1);
    this.cdr.markForCheck();
  }

  clearPhrase(): void {
    this.phraseItems = [];
    this.cdr.markForCheck();
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

  /** Lock handling */

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
  trackByPhrase(index: number): number { return index; }
}
