import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap, BehaviorSubject } from 'rxjs';
import { StorageService } from './storage.service';

const AUTH_API = 'http://localhost:8080/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  

  constructor(private http: HttpClient, private storageService: StorageService) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'login',
      {
        email,
        password,
      },
      httpOptions
    ).pipe(
      tap((response: any) => {
        this.storageService.saveToken(response.token);
      })
    )
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

  registerDoctor(
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
    postalCode: string,
    licenseNumber: string,
    specialization: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'register/doctor',
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
        postalCode,
        licenseNumber,
        specialization
      },
      httpOptions
    );
  }
}
