import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8080/api/user/';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  getUserByEmail(email: string): Observable<any> {
    return this.http.get(API_URL + 'email/' + email);
  }

  getUserByPesel(pesel: string): Observable<any> {
    return this.http.get(API_URL + 'pesel/' + pesel);
  }

  getAllPatients(): Observable<any> {
    return this.http.get(API_URL + 'all');
  }

  getCurrentUser(): Observable<any> {
    return this.http.get(API_URL + 'current');
  }

  getUserById(id: number): Observable<any> {
    return this.http.get(API_URL + id);
  }
}
