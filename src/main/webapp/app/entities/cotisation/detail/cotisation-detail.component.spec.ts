import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CotisationDetailComponent } from './cotisation-detail.component';

describe('Cotisation Management Detail Component', () => {
  let comp: CotisationDetailComponent;
  let fixture: ComponentFixture<CotisationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CotisationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cotisation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CotisationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CotisationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cotisation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cotisation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
