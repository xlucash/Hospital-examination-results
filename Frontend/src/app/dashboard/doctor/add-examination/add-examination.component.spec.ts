import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddExaminationComponent } from './add-examination.component';

describe('AddExaminationComponent', () => {
  let component: AddExaminationComponent;
  let fixture: ComponentFixture<AddExaminationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddExaminationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddExaminationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
