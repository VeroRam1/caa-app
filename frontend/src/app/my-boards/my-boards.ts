// my-boards.component.ts
import {
  Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef, inject
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { MenuModule } from 'primeng/menu';
import { DialogModule } from 'primeng/dialog';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService, MenuItem } from 'primeng/api';
import { BoardService, Board } from '../services/boardService';
import { AuthService } from '../services/auth-service';

interface BoardCategory {
  label: string;
  boards: Board[];
}

@Component({
  selector: 'app-my-boards',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ConfirmationService, MessageService],
  imports: [
    CommonModule,
    ButtonModule,
    ToolbarModule,
    MenuModule,
    DialogModule,
    ConfirmPopupModule,
    ToastModule
  ],
  templateUrl: './my-boards.html',
  styleUrl: './my-boards.scss'
})
export class MyBoardsComponent implements OnInit {

  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);

  menuItems: MenuItem[] = [];
  predefinedBoards: Board[] = [];
  myBoards: Board[] = [];
  loadingPredefined: boolean = true;
  loadingMine: boolean = true;
  copying: boolean = false;

  get groupedPredefinedBoards(): BoardCategory[] {
    const CategoryNames = [
      { label: 'General', keywords: ['básico', 'general'] },
      { label: 'Alimentos', keywords: ['alimentos'] },
      { label: 'Emociones', keywords: ['emociones'] },
      { label: 'Lugares', keywords: ['lugares'] },
      { label: 'Personas', keywords: ['personas'] },
      { label: 'Acciones', keywords: ['acciones'] }
    ];

    return CategoryNames
    .map(category => ({
      label: category.label,
      boards: this.predefinedBoards.filter(board =>

        category.keywords.some(keyword => 
          board.name.toLowerCase().includes(keyword)
        )
      )
    }))
    .filter(category => category.boards.length > 0);
    
  }

  constructor(
    private boardService: BoardService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    }
    this.initMenu();
    this.loadPredefinedBoards();
    this.loadMyBoards();
  }

  initMenu(): void {
    this.menuItems = [
      { label: 'Sobre nosotros', icon: 'pi pi-info-circle', command: () => {} },
      { label: 'Manual de usuario', icon: 'pi pi-book', command: () => {} },
      { label: 'Tutoriales', icon: 'pi pi-play-circle', command: () => {} },
      { label: 'Contacto', icon: 'pi pi-envelope', command: () => {} },
      { separator: true },
      { label: 'Configuración', icon: 'pi pi-cog', command: () => {} }
    ];
  }

  loadPredefinedBoards(): void {
    this.loadingPredefined = true;
    this.cdr.markForCheck();
    this.boardService.getPredefinedBoards().subscribe({
      next: (boards) => {
        this.predefinedBoards = boards;
        this.loadingPredefined = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loadingPredefined = false;
        this.cdr.markForCheck();
      }
    });
  }

  loadMyBoards(): void {
    this.loadingMine = true;
    this.cdr.markForCheck();
    this.boardService.getMyBoards().subscribe({
      next: (boards) => {
        this.myBoards = boards;
        this.loadingMine = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loadingMine = false;
        this.cdr.markForCheck();
      }
    });
  }

  onViewBoard(board: Board): void {
    this.router.navigate(['/board', board.id], { 
      queryParams: { mode: 'tutor' } 
    });
  }

  onCopyAndEdit(board: Board): void {
    this.copying = true;
    this.cdr.markForCheck();
    this.boardService.copyBoard(board.id).subscribe({
      next: (response) => {
        const newBoardId = response.data?.id;
        this.copying = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Tablero copiado',
          detail: `Se ha creado una copia editable de "${board.name}"`
        });
        this.cdr.markForCheck();
        // Navigate directly to the editor
        if (newBoardId) {
          this.router.navigate(['/board-editor', newBoardId]);
        } else {
          this.loadMyBoards();
        }
      },
      error: () => {
        this.copying = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo copiar el tablero. Inténtalo de nuevo.'
        });
        this.cdr.markForCheck();
      }
    });
  }

  onEditBoard(board: Board): void {
    this.router.navigate(['/board-editor', board.id]);
  }

  confirmDeleteBoard(event: Event, board: Board): void {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: `¿Eliminar el tablero "${board.name}"? Esta acción no se puede deshacer.`,
      accept: () => this.deleteBoard(board)
    });
  }

  deleteBoard(board: Board): void {
    this.boardService.deleteBoard(board.id).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Tablero eliminado',
          detail: `"${board.name}" ha sido eliminado.`
        });
        setTimeout(() => this.loadMyBoards(), 0);
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudo eliminar el tablero.'
        });
      }
    });
  }

  onBack(): void {
    this.router.navigate(['/dashboard']);
  }

  // Builds small board for preview in the edit module
  getPreviewGrid(board: Board): (string | null)[][] {
    const grid: (string | null)[][] = Array.from(
      { length: board.rows }, () => Array(board.columns).fill(null)
    );
    if (board.pictograms) {
      for (const p of board.pictograms) {
        if (p.positionY < board.rows && p.positionX < board.columns) {
          grid[p.positionY][p.positionX] = p.pictogramUrl;
        }
      }
    }
    return grid;
  }

  getLevelLabel(level: number): string {
    const labels: Record<number, string> = {
      1: 'Nivel 1',
      2: 'Nivel 2',
      3: 'Nivel 3'
    };
    return labels[level] || `Nivel ${level}`;
  }
}
