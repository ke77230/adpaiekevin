import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConventionCollectiveService } from '../service/convention-collective.service';

import { ConventionCollectiveComponent } from './convention-collective.component';

describe('ConventionCollective Management Component', () => {
  let comp: ConventionCollectiveComponent;
  let fixture: ComponentFixture<ConventionCollectiveComponent>;
  let service: ConventionCollectiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'convention-collective', component: ConventionCollectiveComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [ConventionCollectiveComponent],
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
      .overrideTemplate(ConventionCollectiveComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConventionCollectiveComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConventionCollectiveService);

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
    expect(comp.conventionCollectives?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to conventionCollectiveService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getConventionCollectiveIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getConventionCollectiveIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
