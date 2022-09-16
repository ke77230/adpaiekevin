import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CongeDetailComponent } from './conge-detail.component';

describe('Conge Management Detail Component', () => {
  let comp: CongeDetailComponent;
  let fixture: ComponentFixture<CongeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CongeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ conge: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CongeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CongeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load conge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.conge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
