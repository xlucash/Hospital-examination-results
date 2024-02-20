import { Component } from '@angular/core';
import { AuthService } from '../../../_services/auth.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-add-doctor',
  templateUrl: './add-doctor.component.html',
  styleUrl: './add-doctor.component.css'
})
export class AddDoctorComponent {
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
    postalCode: null,
    licenseNumber: null,
    specialization: null
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    public dialogRef: MatDialogRef<AddDoctorComponent>,
    ) { }

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
      postalCode,
      licenseNumber,
      specialization
    } = this.form;

    this.authService.registerDoctor(
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
      postalCode,
      licenseNumber,
      specialization
    ).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
        this.dialogRef.close(data);
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

  closeDialog(): void {
    this.dialogRef.close();
  }
}
