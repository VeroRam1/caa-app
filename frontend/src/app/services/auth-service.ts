import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth'; 

  constructor(private http: HttpClient) {}
  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password });
  }
  register(name: string, email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { name, email, password });
  }
  
  // Save the token in localStorage 
  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  // Save tutor name in localStorage
  saveTutorName(name: string): void {
    localStorage.setItem('tutorName', name);
  }

  getTutorName(): string {
    return localStorage.getItem('tutorName') || '';
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('tutorName');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

}
