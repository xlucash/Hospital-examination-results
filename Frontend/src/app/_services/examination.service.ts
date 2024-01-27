import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const EXAMINATION_API = 'http://localhost:8080/api/examination-result';

@Injectable({
  providedIn: 'root'
})
export class ExaminationService {

  constructor(private http: HttpClient) { }

  getAllExaminationResults(): Observable<any> {
    return this.http.get(EXAMINATION_API + '/all');
  }

  getExaminationResult(id: number): Observable<any> {
    return this.http.get(EXAMINATION_API + '/' + id);
  }

  getExaminationResultPdf(id: number): Observable<Blob> {
    return this.http.get(EXAMINATION_API + '/' + id + '/pdf', { responseType: 'blob' });
  }

  getExaminationResultsByPatient(type: string): Observable<any> {
    return this.http.get(EXAMINATION_API + '/patient/' + type);
  }

  getExaminationResultsByDoctor(type: string): Observable<any> {
    return this.http.get(EXAMINATION_API + '/doctor/' + type);
  }

  saveExaminationResult(examinationDto: any, images: File[]) {
    const formData = new FormData();
    formData.append('examinationResult', new Blob([JSON.stringify(examinationDto)], {
      type: 'application/json'
    }));

    images.forEach((file, index) => {
      formData.append(`image`, file, file.name);
    });

    return this.http.post(EXAMINATION_API, formData);
  }
}
