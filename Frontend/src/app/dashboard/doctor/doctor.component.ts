import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { AddExaminationComponent } from './add-examination/add-examination.component';
import { ExaminationService } from '../../_services/examination.service';
import { UserService } from '../../_services/user.service';
import { AddDoctorComponent } from './add-doctor/add-doctor.component';

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrl: './doctor.component.css'
})
export class DoctorComponent implements OnInit {
  displayedColumns: string[] = ['name', 'surname', 'dateOfBirth', 'pesel', 'email'];
  patients = new MatTableDataSource<any>([]);
  doctors = new MatTableDataSource<any>([]);

  @ViewChild(MatPaginator, { static: true }) patientsPaginator!: MatPaginator;
  @ViewChild(MatPaginator, { static: true }) doctorsPaginator!: MatPaginator;

  constructor(
    public dialog : MatDialog,
    private userService : UserService,
    private _snackBar : MatSnackBar
  ) { }

  ngOnInit() {
    this.userService.getAllPatients().subscribe(
      data => {
        const filteredPatients = data.filter((d: { roles: any[]; }) => d.roles.some(role => role.name === 'ROLE_USER'));
        const filteredDoctors = data.filter((d: { roles: any[]; }) => d.roles.some(role => role.name === 'ROLE_DOCTOR'));
        this.doctors.data = filteredDoctors.map((doctor: {
          email: any; id: any; person: {
          pesel: any; name: any; surname: any; dateOfBirth: any; }; 
        }) => ({
          id: doctor.id,
          name: doctor.person.name,
          surname: doctor.person.surname,
          dateOfBirth: doctor.person.dateOfBirth,
          pesel: doctor.person.pesel,
          email: doctor.email,
        }));
        this.patients.data = filteredPatients.map((patient: {
          email: any; id: any; person: {
          pesel: any; name: any; surname: any; dateOfBirth: any; }; 
        }) => ({
          id: patient.id,
          name: patient.person.name,
          surname: patient.person.surname,
          dateOfBirth: patient.person.dateOfBirth,
          pesel: patient.person.pesel,
          email: patient.email,
        }));
        this.patients.paginator = this.patientsPaginator;
        this.doctors.paginator = this.doctorsPaginator;
        console.log(data);
      },
      error => {
        console.error(error);
      }
    );
  }

  addResults() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    const dialogRef = this.dialog.open(AddExaminationComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(data => {
      if(data) {
        this.openSnackBar("Pomyślnie dodano wynik badania.", "Zamknij")
      }
    })
  }

  addDoctor() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    const dialogRef = this.dialog.open(AddDoctorComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(data => {
      if(data) {
        this.openSnackBar("Pomyślnie utworzono konto dla lekarza.", "Zamknij")
      }
    })
  }


  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.patients.filter = filterValue.trim().toLowerCase();

    if (this.patients.paginator) {
      this.patients.paginator.firstPage();
    }
  }
}