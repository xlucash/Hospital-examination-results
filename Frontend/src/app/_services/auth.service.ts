import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:8080/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'login',
      {
        email,
        password,
      },
      httpOptions
    );
  }

  register(
    email: string, 
    password: string, 
    name: string, 
    surname: string, 
    dateOfBirth: string, 
    pesel: string, 
    phoneNumber: string,
    streetAddress: string, 
    house: string, 
    apartment: string, 
    city: string, 
    postalCode: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'register',
      {
        email,
        password,
        name,
        surname,
        dateOfBirth,
        pesel,
        streetAddress,
        phoneNumber,
        house,
        apartment,
        city,
        postalCode
      },
      httpOptions
    );
  }

  logout(): Observable<any> {
    return this.http.post(AUTH_API + 'logout', { }, httpOptions);
  }
}
