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
}
