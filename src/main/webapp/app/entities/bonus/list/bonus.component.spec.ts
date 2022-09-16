import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BonusService } from '../service/bonus.service';

import { BonusComponent } from './bonus.component';

describe('Bonus Management Component', () => {
  let comp: BonusComponent;
  let fixture: ComponentFixture<BonusComponent>;
  let service: BonusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'bonus', component: BonusComponent }]), HttpClientTestingModule],
      declarations: [BonusComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(BonusComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BonusComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BonusService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.bonuses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to bonusService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getBonusIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getBonusIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
