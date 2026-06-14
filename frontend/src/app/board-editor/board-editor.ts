import { ChangeDetectionStrategy, ChangeDetectorRef, Component, inject , OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, EventType, Router } from '@angular/router';
import { CdkDragDrop, CdkDrag, CdkDropList, moveItemInArray } from '@angular/cdk/drag-drop';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { BoardPictogram } from '../services/boardService';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Board } from '../services/boardService';
import { AuthService } from '../services/auth-service';
import { BoardService } from '../services/boardService';
import { Toast } from "primeng/toast";


interface ArasaacResult {
  _id: number;
  keywords: {keyword: string}[]; // Array of objects with keyword (that is a string)
}

// Flat cell model
interface GridCell {
  pictogram: BoardPictogram | null;
  row: number;
  col: number;
}

@Component({
  selector: 'app-board-editor',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [MessageService, ConfirmationService],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    ToolbarModule,
    InputTextModule,
    DialogModule,
    ToastModule,
    ConfirmPopupModule,
    CdkDrag,
    CdkDropList,
    ConfirmPopupModule
],
  templateUrl: './board-editor.html',
  styleUrl: './board-editor.scss',
})

export class BoardEditor implements OnInit {
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  protected readonly Math = Math;
  private readonly BACKEND_SEARCH = 'http://localhost:8080/api/arasaac/search';

  board: Board | null = null;
  loading: boolean = true;
  saving: boolean = false;
  error: string = '';
  hasUnsavedChanges: boolean = false;

  // Board settings (editable)
  boardName: string = '';
  boardRows: number = 3;
  boardColumns: number = 4;

  // Flat list of cells for drag and drop
  cells: GridCell[] = [];

  // ARASAAC search
  searchKeyword: string = '';
  searchResults: ArasaacResult[] = [];
  searching: boolean = false;
  sidebarOpen: boolean = true;

  // Pictogram being dragged from search results
  draggingFromSearch: ArasaacResult | null = null;

  private readonly ARASAAC_API = 'https://api.arasaac.org/v1/pictograms/es/search';
  private readonly ARASAAC_IMG = 'https://static.arasaac.org/pictograms';
  private searchTimeout: ReturnType<typeof setTimeout> | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private boardService: BoardService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
  ){}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    }
    const boardId = Number(this.route.snapshot.paramMap.get('id'));
    if (boardId) {
      this.loadBoard(boardId);
    } else {
      this.error = 'Tablero no encontrado';
      this.loading = false;
    }
  }

  loadBoard(id: number): void {
    this.loading = true;
    this.cdr.markForCheck();
    this.boardService.getBoardById(id).subscribe({
      next: (board) => {
        this.board = board;
        this.boardName = board.name;
        this.boardRows = board.rows;
        this.boardColumns = board.columns;
        this.buildCells(board);
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
  // Builds flat array of cells from the board data
  buildCells(board: Board): void {
    const cells: GridCell[] = [];
    for (let row = 0; row < board.rows; row++) {
      for (let col = 0; col < board.columns; col++) {
        const pictogram = board.pictograms.find(
          p => p.positionX === col && p.positionY === row
        ) || null;
        cells.push({ pictogram, row, col });
      }
    }
    this.cells = cells;
  }

  /*********************** Grid helpers *****************************/
  // Creates a CSS grid template string based on the number of rows and columns of the board
  get gridStyle() {
    return `repeat(${this.boardColumns}, 1fr)`;
  }

  getCellsForRow(row: number): GridCell[] {
    return this.cells.filter(cell => cell.row === row);
  }

  getRowIndex(): number[] {
    return Array.from({ length : this.boardRows }, (_, i) => i); // returns an array of consecutive numbers from 0 to boardRows - 1
  }

  /********************** Drag and Drop Methods ***********************/
  // Called when a pictogram is dropped onto a cell in the grid. It receives the drag event and the target cell as parameters. 
  onDropInGrid(event: CdkDragDrop<GridCell[]>): void {
    if (event.previousIndex === event.currentIndex) return;
    
    const fromCell = this.cells[event.previousIndex];
    const toCell = this.cells[event.currentIndex];

    // Swap pictograms between cells
    const temp = toCell.pictogram;
    toCell.pictogram = fromCell.pictogram;
    fromCell.pictogram = temp;

    this.hasUnsavedChanges = true;
    this.cdr.markForCheck();
  }

  // Called when a pictogram from the search results is dropped onto a cell in the grid. It receives the drag event and the target cell as parameters.
  onDropFromSearch(event: DragEvent, targetCell: GridCell): void {
    event.preventDefault();
    if (!this.draggingFromSearch) return;

    const result = this.draggingFromSearch;
    const id = result._id || result._id;

    targetCell.pictogram = {
      id: 0,
      pictogramId: id,
      pictogramUrl: this.getPictogramUrl(result),
      text: this.getPictogramLabel(result),
      positionX: targetCell.col,
      positionY: targetCell.row,
      backgroundColor: null
    };

    this.draggingFromSearch = null;
    this.hasUnsavedChanges = true;
    this.cdr.markForCheck();
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault(); // Necessary to allow dropping
  }

  onSearchResultDragStart(result: ArasaacResult): void {
    this.draggingFromSearch = result;
  }

  onSearchResultDragEnd(): void {
    this.draggingFromSearch = null;
  }

  removePictogram(cell: GridCell): void {
    cell.pictogram = null;
    this.hasUnsavedChanges = true;
    this.cdr.markForCheck();
  }

  /********************** ARASAAC Search Methods ***********************/
  onSearchInput(): void {
    if (this.searchTimeout) clearTimeout(this.searchTimeout);
    if (!this.searchKeyword.trim()){
      this.searchResults = [];
      this.cdr.markForCheck();
      return;
    }
    this.searchTimeout = setTimeout(() => this.searchPictograms(), 400); // Debounce search by 400ms
  }

  searchPictograms(): void {
    this.searching = true;
    this.cdr.markForCheck();
    fetch(`${this.BACKEND_SEARCH}?keyword=${encodeURIComponent(this.searchKeyword.trim())}`)
      .then(response => response.json())
      .then((results: any[]) => {
        // Show max 20 results
        this.searchResults = results.slice(0, 20);
        this.searching = false;
        this.cdr.markForCheck();
      })
      .catch(() => {
        this.searching = false;
        this.searchResults = [];
        this.cdr.markForCheck();
      });
  }

  getPictogramLabel(result: any): string {
    // ArasaacPictogramResponseDTO tiene getKeywordsEs() pero en JSON
    // llega como keywordsEs (lista de strings) o keywords (objeto crudo)
    if (result.keywordsEs && result.keywordsEs.length > 0) {
      return result.keywordsEs[0];
    }
    // Fallback: parsear keywords manualmente
    if (result.keywords && Array.isArray(result.keywords)) {
      const first = result.keywords[0];
      if (typeof first === 'string') return first;
      if (first?.keyword) return first.keyword;
    }
    return `pictograma ${result.id || result._id}`;
  }

  getPictogramUrl(result: any): string {
    const id = result.id || result._id;
    return `https://api.arasaac.org/v1/pictograms/${id}?download=false&plural=false&color=true`;
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
    this.cdr.markForCheck();
  }

  /************************ Resize Grid ************************/
  onResizeGrid(): void {
    if (!this.board) return;
    // Check if new size can accommodate existing pictograms
    const outOfBounds = this.cells.filter(c =>
      c.pictogram && (c.row >= this.boardRows || c.col >= this.boardColumns)
    );
    if (outOfBounds.length > 0) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Pictogramas fuera de límites',
        detail: `${outOfBounds.length} pictogramas quedarán fuera de los límites del tablero y se eliminarán.`,
        life: 5000
      });
    }
    // Rebuild cells with new dimensions
    const newCells: GridCell[] = [];
    for (let row= 0; row < this.boardRows; row++) {
      for (let col = 0; col < this.boardColumns; col++) {
        const existingCell = this.cells.find(c => c.row === row && c.col === col);
        newCells.push({
          row, col, pictogram: existingCell?.pictogram || null
        });
      }
    } 
    this.cells = newCells;
    this.hasUnsavedChanges = true;
    this.cdr.markForCheck();
  }

  onSave(): void {
  if (!this.board || !this.boardName.trim()) return;
    this.saving = true;
    this.cdr.markForCheck();

    const boardId = this.board.id;

    const pictograms = this.cells
      .filter(c => c.pictogram !== null)
      .map(c => ({
        pictogramId: c.pictogram!.pictogramId,
        pictogramUrl: c.pictogram!.pictogramUrl,
        text: c.pictogram!.text,
        positionX: c.col,
        positionY: c.row,
        backgroundColor: c.pictogram!.backgroundColor
      }));

    // 1º: actualizar pictogramas (ya dentro de los nuevos límites)
    this.boardService.updateBoardPictograms(boardId, { pictograms }).subscribe({
      next: () => {
        // 2º: ahora sí, redimensionar/actualizar nombre y dimensiones
        const updateData = {
          name: this.boardName,
          description: this.board!.description || '',
          rows: this.boardRows,
          columns: this.boardColumns,
          level: this.board!.level,
          isPredefined: false
        };

        this.boardService.updateBoard(boardId, updateData).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Guardado',
              detail: 'El tablero se ha guardado correctamente.'
            });
            this.hasUnsavedChanges = false;
            this.saving = false;
            this.cdr.markForCheck();
          },
          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'No se pudo actualizar el tablero.'
            });
            this.saving = false;
            this.cdr.markForCheck();
          }
        });
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'No se pudieron guardar los pictogramas.'
        });
        this.saving = false;
        this.cdr.markForCheck();
      }
    });
  }

  onCancel(event: Event): void {
    if (this.hasUnsavedChanges) {
      this.confirmationService.confirm({
        target: event.target as EventTarget,
        message: '¿Tienes cambios sin guardar. ¿Salir de todas formas?',
        accept: () => {
          this.router.navigate(['/my-boards']);
        }
      });
    } else {
      this.router.navigate(['/my-boards']);
    }
  }

  trackByCell(index: number, cell: GridCell): string {
    return `${cell.row}-${cell.col}`;
  }

}