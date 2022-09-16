import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FicheDePaieDetailComponent } from './fiche-de-paie-detail.component';

describe('FicheDePaie Management Detail Component', () => {
  let comp: FicheDePaieDetailComponent;
  let fixture: ComponentFixture<FicheDePaieDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FicheDePaieDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ficheDePaie: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FicheDePaieDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FicheDePaieDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ficheDePaie on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ficheDePaie).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
