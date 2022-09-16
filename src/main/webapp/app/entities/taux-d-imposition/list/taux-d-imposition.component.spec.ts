import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TauxDImpositionService } from '../service/taux-d-imposition.service';

import { TauxDImpositionComponent } from './taux-d-imposition.component';

describe('TauxDImposition Management Component', () => {
  let comp: TauxDImpositionComponent;
  let fixture: ComponentFixture<TauxDImpositionComponent>;
  let service: TauxDImpositionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'taux-d-imposition', component: TauxDImpositionComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [TauxDImpositionComponent],
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
      .overrideTemplate(TauxDImpositionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TauxDImpositionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TauxDImpositionService);

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
    expect(comp.tauxDImpositions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to tauxDImpositionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getTauxDImpositionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTauxDImpositionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
