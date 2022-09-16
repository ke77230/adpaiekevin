import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmployeurDetailComponent } from './employeur-detail.component';

describe('Employeur Management Detail Component', () => {
  let comp: EmployeurDetailComponent;
  let fixture: ComponentFixture<EmployeurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employeur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmployeurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmployeurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employeur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employeur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
