import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MentionDetailComponent } from './mention-detail.component';

describe('Mention Management Detail Component', () => {
  let comp: MentionDetailComponent;
  let fixture: ComponentFixture<MentionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MentionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ mention: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MentionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MentionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mention on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.mention).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
