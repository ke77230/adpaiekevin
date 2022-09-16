import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConventionCollectiveDetailComponent } from './convention-collective-detail.component';

describe('ConventionCollective Management Detail Component', () => {
  let comp: ConventionCollectiveDetailComponent;
  let fixture: ComponentFixture<ConventionCollectiveDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConventionCollectiveDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ conventionCollective: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConventionCollectiveDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConventionCollectiveDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load conventionCollective on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.conventionCollective).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
