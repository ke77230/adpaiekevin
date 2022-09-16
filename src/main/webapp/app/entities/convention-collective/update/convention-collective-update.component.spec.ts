import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConventionCollectiveFormService } from './convention-collective-form.service';
import { ConventionCollectiveService } from '../service/convention-collective.service';
import { IConventionCollective } from '../convention-collective.model';

import { ConventionCollectiveUpdateComponent } from './convention-collective-update.component';

describe('ConventionCollective Management Update Component', () => {
  let comp: ConventionCollectiveUpdateComponent;
  let fixture: ComponentFixture<ConventionCollectiveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conventionCollectiveFormService: ConventionCollectiveFormService;
  let conventionCollectiveService: ConventionCollectiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConventionCollectiveUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ConventionCollectiveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConventionCollectiveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conventionCollectiveFormService = TestBed.inject(ConventionCollectiveFormService);
    conventionCollectiveService = TestBed.inject(ConventionCollectiveService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const conventionCollective: IConventionCollective = { id: 456 };

      activatedRoute.data = of({ conventionCollective });
      comp.ngOnInit();

      expect(comp.conventionCollective).toEqual(conventionCollective);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConventionCollective>>();
      const conventionCollective = { id: 123 };
      jest.spyOn(conventionCollectiveFormService, 'getConventionCollective').mockReturnValue(conventionCollective);
      jest.spyOn(conventionCollectiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conventionCollective });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conventionCollective }));
      saveSubject.complete();

      // THEN
      expect(conventionCollectiveFormService.getConventionCollective).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(conventionCollectiveService.update).toHaveBeenCalledWith(expect.objectContaining(conventionCollective));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConventionCollective>>();
      const conventionCollective = { id: 123 };
      jest.spyOn(conventionCollectiveFormService, 'getConventionCollective').mockReturnValue({ id: null });
      jest.spyOn(conventionCollectiveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conventionCollective: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conventionCollective }));
      saveSubject.complete();

      // THEN
      expect(conventionCollectiveFormService.getConventionCollective).toHaveBeenCalled();
      expect(conventionCollectiveService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConventionCollective>>();
      const conventionCollective = { id: 123 };
      jest.spyOn(conventionCollectiveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conventionCollective });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conventionCollectiveService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
