import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

const USER_KEY = 'auth-user';
const TOKEN = 'token';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private loggedIn = new BehaviorSubject<boolean>(this.isLoggedIn());

  constructor() {}

  clean(): void {
    window.sessionStorage.clear();

    this.updateLoggedInStatus(false);
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public saveToken(token: any): void {
    window.sessionStorage.removeItem(TOKEN);
    window.sessionStorage.setItem(TOKEN, token);
  }

  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }

    return {};
  }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return true;
    }

    return false;
  }

  getLoggedInStatus() {
    return this.loggedIn.asObservable();
  }

  updateLoggedInStatus(status: boolean) {
    this.loggedIn.next(status);
  }
}