import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CotisationFormService } from './cotisation-form.service';
import { CotisationService } from '../service/cotisation.service';
import { ICotisation } from '../cotisation.model';

import { CotisationUpdateComponent } from './cotisation-update.component';

describe('Cotisation Management Update Component', () => {
  let comp: CotisationUpdateComponent;
  let fixture: ComponentFixture<CotisationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cotisationFormService: CotisationFormService;
  let cotisationService: CotisationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CotisationUpdateComponent],
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
      .overrideTemplate(CotisationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CotisationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cotisationFormService = TestBed.inject(CotisationFormService);
    cotisationService = TestBed.inject(CotisationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cotisation: ICotisation = { id: 456 };

      activatedRoute.data = of({ cotisation });
      comp.ngOnInit();

      expect(comp.cotisation).toEqual(cotisation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICotisation>>();
      const cotisation = { id: 123 };
      jest.spyOn(cotisationFormService, 'getCotisation').mockReturnValue(cotisation);
      jest.spyOn(cotisationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cotisation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cotisation }));
      saveSubject.complete();

      // THEN
      expect(cotisationFormService.getCotisation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cotisationService.update).toHaveBeenCalledWith(expect.objectContaining(cotisation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICotisation>>();
      const cotisation = { id: 123 };
      jest.spyOn(cotisationFormService, 'getCotisation').mockReturnValue({ id: null });
      jest.spyOn(cotisationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cotisation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cotisation }));
      saveSubject.complete();

      // THEN
      expect(cotisationFormService.getCotisation).toHaveBeenCalled();
      expect(cotisationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICotisation>>();
      const cotisation = { id: 123 };
      jest.spyOn(cotisationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cotisation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cotisationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
