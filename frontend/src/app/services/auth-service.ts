import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) { }
  
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

  saveActiveProfile(profileId: number, level: string) {
    localStorage.setItem('activeProfileId', profileId.toString());
    localStorage.setItem('activeProfileLevel', level);

  }

  getActiveProfileLevel(): string {
    return localStorage.getItem('activeProfileLevel') || 'LEVEL_1';
  }

  getActiveProfileId(): number | null {
    const id = localStorage.getItem('activeProfileId');
    return id ? Number(id) : null;
  }

  clearActiveProfile() {
    localStorage.removeItem('activeProfileId');
    localStorage.removeItem('activeProfileLevel');
  }
}
