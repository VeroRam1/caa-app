import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ChildProfile {
  id: number;
  name: string;
  birthDate: string | null;
  photoUrl: string | null;
  level: 'LEVEL_1' | 'LEVEL_2' | 'LEVEL_3';
  tutorId: number;
  boardCount: number;
  assignedBoards: any[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateChildProfileRequest {
  name: string;
  birthDate: string | null;
  photoUrl: string | null;
  level: string;
}

interface ApiResponse<T> {
    success: boolean;
    data: T;
    message: string;
    timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChildProfileService {
    private apiUrl = `${environment.apiUrl}/child-profiles`;
    private backendUrl = environment.apiUrl.replace('/api', '');

    constructor(private http: HttpClient) {}

    // Get all child profiles for the logged-in tutor
    getAllChildProfiles(): Observable<ChildProfile[]> {
        return this.http.get<ChildProfile[]>(this.apiUrl);
    }
    
    getChildProfileById(profileId: number): Observable<any> {
        return this.http.get(`${this.apiUrl}/${profileId}`);
    }

    // Create a new child profile
    createProfile(request: CreateChildProfileRequest): Observable<ApiResponse<ChildProfile>> {
        return this.http.post<ApiResponse<ChildProfile>>(`${this.apiUrl}`, request);
    }

    // Delete a child profile by ID
    deleteProfile(profileId: number): Observable<ApiResponse<void>> {
        return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${profileId}`);
    }

    // Edit a child profile by ID
    updateLevel(profileId: number, level: string): Observable<ApiResponse<ChildProfile>> {
        return this.http.patch<ApiResponse<ChildProfile>>(`${this.apiUrl}/${profileId}/level`, { level });
    }

    // Update a child profile's information
    updateProfile(profileId: number, request: any): Observable<any> {
        return this.http.put(`${this.apiUrl}/${profileId}`, request);
    }

    // Upload a photo for a child profile
    uploadPhoto(profileId: number, file: File): Observable<any> {
        const formData = new FormData();
        formData.append('file', file);
        return this.http.post(`${this.apiUrl}/${profileId}/photo`, formData);
    }

    getPhotoUrl(photoUrl: string | null): string |null {
        if (!photoUrl) return null;
        if (photoUrl.startsWith('http')) return photoUrl;
        return this.backendUrl + photoUrl;
    }

    assignBoard(BoardId: number, profileId: number): Observable<any> {
        return this.http.post(`${this.apiUrl}/${BoardId}/boards/${profileId}`, {});
    }

    removeBoard(BoardId: number, profileId: number): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${BoardId}/boards/${profileId}`);
    }

}