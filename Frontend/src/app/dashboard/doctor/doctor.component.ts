import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { AddExaminationComponent } from './add-examination/add-examination.component';
import { ExaminationService } from '../../_services/examination.service';
import { UserService } from '../../_services/user.service';

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrl: './doctor.component.css'
})
export class DoctorComponent implements OnInit {

  displayedColumns: string[] = ['name', 'surname', 'dateOfBirth', 'pesel', 'email'];
  patients = new MatTableDataSource<any>([]);

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  constructor(
    public dialog : MatDialog,
    private userService : UserService,
    private _snackBar : MatSnackBar
  ) { }

  ngOnInit() {
    this.userService.getAllPatients().subscribe(
      data => {
        const filteredPatients = data.filter((d: { roles: any[]; }) => d.roles.some(role => role.name === 'ROLE_USER'));
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
        this.patients.paginator = this.paginator;
        console.log(data);
      },
      error => {
        console.error(error);
      }
    );
  }

  addDoctor() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      title : 'Register doctor',
      buttonName : 'Register'
    }

    const dialogRef = this.dialog.open(AddExaminationComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(data => {
      if(data) {
        this.openSnackBar("Registration of doctor is successful.", "OK")
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