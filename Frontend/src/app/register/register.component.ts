import { Component } from '@angular/core';
import { AuthService } from '../_services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  form: any = {
    email: null,
    password: null,
    name: null,
    surname: null,
    dateOfBirth: null,
    pesel: null,
    phoneNumber: null,
    streetAddress: null,
    house: null,
    apartment: null,
    city: null,
    postalCode: null
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService) { }

  onSubmit(): void {
    const { 
      email, 
      password, 
      name, 
      surname, 
      dateOfBirth, 
      pesel, 
      phoneNumber,
      streetAddress, 
      house, 
      apartment, 
      city, 
      postalCode 
    } = this.form;

    this.authService.register(
      email,
      password,
      name,
      surname,
      dateOfBirth,
      pesel,
      phoneNumber,
      streetAddress,
      house,
      apartment,
      city,
      postalCode
    ).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    });
  }

  formatPhoneNumber() {
    if (this.form.phoneNumber) {
      this.form.phoneNumber = this.form.phoneNumber.replace(/\D/g, '').replace(/(\d{3})(\d{3})(\d{3})/, '$1-$2-$3');
    }
  }

  formatPostalCode() {
    if (this.form.postalCode) {
      this.form.postalCode = this.form.postalCode.replace(/\D/g, '').replace(/(\d{2})(\d{3})/, '$1-$2');
    }
  }
}