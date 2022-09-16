import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FicheDePaieService } from '../service/fiche-de-paie.service';

import { FicheDePaieComponent } from './fiche-de-paie.component';

describe('FicheDePaie Management Component', () => {
  let comp: FicheDePaieComponent;
  let fixture: ComponentFixture<FicheDePaieComponent>;
  let service: FicheDePaieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'fiche-de-paie', component: FicheDePaieComponent }]), HttpClientTestingModule],
      declarations: [FicheDePaieComponent],
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
      .overrideTemplate(FicheDePaieComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FicheDePaieComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FicheDePaieService);

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
    expect(comp.ficheDePaies?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ficheDePaieService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFicheDePaieIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFicheDePaieIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
