import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContratService } from '../service/contrat.service';

import { ContratComponent } from './contrat.component';

describe('Contrat Management Component', () => {
  let comp: ContratComponent;
  let fixture: ComponentFixture<ContratComponent>;
  let service: ContratService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'contrat', component: ContratComponent }]), HttpClientTestingModule],
      declarations: [ContratComponent],
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
      .overrideTemplate(ContratComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContratComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ContratService);

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
    expect(comp.contrats?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to contratService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getContratIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getContratIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
