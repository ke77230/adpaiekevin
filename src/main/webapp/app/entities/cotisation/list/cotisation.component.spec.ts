import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CotisationService } from '../service/cotisation.service';

import { CotisationComponent } from './cotisation.component';

describe('Cotisation Management Component', () => {
  let comp: CotisationComponent;
  let fixture: ComponentFixture<CotisationComponent>;
  let service: CotisationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'cotisation', component: CotisationComponent }]), HttpClientTestingModule],
      declarations: [CotisationComponent],
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
      .overrideTemplate(CotisationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CotisationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CotisationService);

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
    expect(comp.cotisations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to cotisationService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCotisationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCotisationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
