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

  saveExaminationResult(examinationRequestDto: any, images: File[]): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('examinationResult', JSON.stringify(examinationRequestDto));
    images.forEach(image => {
      formData.append('image', image);
    });

    return this.http.post(EXAMINATION_API, formData);
  }
}
