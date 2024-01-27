import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ExaminationService } from '../../../_services/examination.service';

@Component({
  selector: 'app-add-examination',
  templateUrl: './add-examination.component.html',
  styleUrl: './add-examination.component.css'
})
export class AddExaminationComponent {
  examinationForm!: FormGroup;
  images: File[] = [];

  constructor(
    private fb: FormBuilder,
    private examinationService: ExaminationService
  ) {
    this.examinationForm = this.fb.group({
      type: ['', Validators.required],
      title: ['', Validators.required],
      description: [''],
      pesel: ['', [Validators.required, Validators.pattern(/^\d{11}$/)]]
    });
  }

  onFileSelect(event: Event): void {
    const element = event.currentTarget as HTMLInputElement;
    let files: FileList | null = element.files;
    if (files) {
      this.images = Array.from(files);
    }
  }

  onSubmit(): void {
    if (this.examinationForm.valid && this.images.length) {
      this.examinationService.saveExaminationResult(this.examinationForm.value, this.images).subscribe(
        response => {
          console.log('Examination result saved successfully', response);
        },
        error => {
          console.error('Error saving the examination result', error);
        }
      );
    } else {
      console.error('Form is not valid or no images have been selected');

    }
  }
}
