import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { StorageService } from '../_services/storage.service';
import { UserService } from '../_services/user.service';
import { ExaminationService } from '../_services/examination.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy, AfterViewInit {
  currentUser: any;
  examinationResults: any[] = [];
  usgResults = new MatTableDataSource<any>([]);
  rentgenResults = new MatTableDataSource<any>([]);
  tkResults = new MatTableDataSource<any>([]);
  mriResults = new MatTableDataSource<any>([]);
  private unsubscribe$ = new Subject<void>();
  displayedColumns: string[] = ['title', 'description', 'examinationDate', 'doctor', 'download'];

  @ViewChild('usgPaginator', {static: false}) usgPaginator!: MatPaginator;
  @ViewChild('rentgenPaginator', {static: false}) rentgenPaginator!: MatPaginator;
  @ViewChild('tkPaginator', {static: false}) tkPaginator!: MatPaginator;
  @ViewChild('mriPaginator', {static: false}) mriPaginator!: MatPaginator;

  constructor(private storageService: StorageService, private examinationResultService: ExaminationService, private userService: UserService) {
  }


  ngOnInit(): void {
    this.currentUser = this.storageService.getUser();

    this.loadExaminationResults();
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  ngAfterViewInit() {
    this.usgResults.paginator = this.usgPaginator;
    this.rentgenResults.paginator = this.rentgenPaginator;
    this.tkResults.paginator = this.tkPaginator;
    this.mriResults.paginator = this.mriPaginator;
  }

  loadExaminationResults(): void {
    this.examinationResultService.getAllExaminationResults().pipe(
      takeUntil(this.unsubscribe$)
    ).subscribe(
      data => {
        this.examinationResults = data;

        this.examinationResults.forEach(result => {
          if (result.doctorId) {
            this.userService.getUserById(result.doctorId).subscribe(
              doctorData => {
                result.doctor = doctorData;
              },
              error => {
                console.error('Error fetching doctor data', error);
              }
            );
          }
        });

        this.usgResults.data = data.filter((result: { type: string; }) => result.type === 'USG');
        this.rentgenResults.data = data.filter((result: { type: string; }) => result.type === 'XRAY');
        this.tkResults.data = data.filter((result: { type: string; }) => result.type === 'CT'); // Nowa linia dla TK
        this.mriResults.data = data.filter((result: { type: string; }) => result.type === 'MRI'); // Nowa linia dla MRI
        console.log(data);

        this.usgResults.paginator = this.usgPaginator;
        this.rentgenResults.paginator = this.rentgenPaginator;
        this.tkResults.paginator = this.tkPaginator;
        this.mriResults.paginator = this.mriPaginator;

        if (this.usgPaginator) {
          this.usgPaginator.firstPage();
        }
        if (this.rentgenPaginator) {
          this.rentgenPaginator.firstPage();
        }
        if (this.tkPaginator) {
          this.tkPaginator.firstPage();
        }
        if (this.mriPaginator) {
          this.mriPaginator.firstPage();
        }
      },
      error => {
        console.error('There was an error!', error);
      }
    );
  }

  downloadPdf(id: number): void {
    this.examinationResultService.getExaminationResultPdf(id).subscribe(data => {
      const blob = new Blob([data], { type: 'application/pdf' });
      
      const downloadURL = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = downloadURL;
      link.download = `badanie_${id}.pdf`;
      link.click();
    }, error => {
      console.error('Error downloading the file');
    });
  }
}