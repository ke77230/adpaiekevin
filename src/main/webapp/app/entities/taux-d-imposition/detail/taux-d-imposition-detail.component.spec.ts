import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TauxDImpositionDetailComponent } from './taux-d-imposition-detail.component';

describe('TauxDImposition Management Detail Component', () => {
  let comp: TauxDImpositionDetailComponent;
  let fixture: ComponentFixture<TauxDImpositionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TauxDImpositionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tauxDImposition: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TauxDImpositionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TauxDImpositionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tauxDImposition on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tauxDImposition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
