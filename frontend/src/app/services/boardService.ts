import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from '../../environments/environment';

export interface BoardPictogram {
    id: number;
    pictogramId: number;
    pictogramUrl: string;
    text: string;
    positionX: number;
    positionY: number;
    backgroundColor: string | null;
}

export interface Board {
    id: number;
    name: string;
    description: string;
    rows: number;
    columns: number;
    level: number;
    isPredefined: boolean;
    category?: string;
    totalCells: number;
    pictogramCount: number;
    pictograms: BoardPictogram[];
    createdAt: string;
    updatedAt: string;
}

export interface UpdatePictogramsRequest {
  pictograms: {
    pictogramId: number;
    pictogramUrl: string;
    text: string;
    positionX: number;
    positionY: number;
    backgroundColor: string | null;
  }[];
}

export interface CreateBoardRequest {
  name: string;
  description: string;
  rows: number;
  columns: number;
  level: number;
  isPredefined: boolean;
  category?: string;
}

@Injectable({
    providedIn: "root",
})
export class BoardService {
  private apiUrl = `${environment.apiUrl}/boards`;

  constructor(private http: HttpClient) { }
  
  // Get boards by Id with all its pictograms
  getBoardById(id: number): Observable<Board> {
    return this.http.get<Board>(`${this.apiUrl}/${id}`);
  }

  // Get all predefined boards with all its pictograms
  getPredefinedBoards(): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiUrl}/predefined`);
  }

  // Get all boards assigned to a user with all its pictograms
  getBoardsByLevel(level: number): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiUrl}/level/${level}`);
  }

  getAllBoards(): Observable<Board[]> {
    return this.http.get<Board[]>(this.apiUrl);
  }

  // Gets predefined boards by level — used for guest page and category sidebar.
  getPredefinedBoardsByLevel(level: number): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiUrl}/level/${level}`);
  }

  // Get the boards assigned to the logged-in user with all its pictograms
  getMyBoards(): Observable<Board[]> {
    return this.http.get<Board[]>(`${this.apiUrl}/myBoards`);
  }

  copyBoard(id: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${id}/copy`, {});
  }

  createBoard(request: CreateBoardRequest): Observable<any> {
    return this.http.post(this.apiUrl, request);
  }

  updateBoard(id: number, data: { name: string; description: string }): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, data);
  }

  resizeBoard(id: number, rows: number, columns: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}/resize`, { rows, columns });
  }

  updateBoardPictograms(id: number, request: UpdatePictogramsRequest): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/pictograms`, request);
  }

  deleteBoard(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}