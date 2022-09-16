import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TauxDImpositionFormService } from './taux-d-imposition-form.service';
import { TauxDImpositionService } from '../service/taux-d-imposition.service';
import { ITauxDImposition } from '../taux-d-imposition.model';

import { TauxDImpositionUpdateComponent } from './taux-d-imposition-update.component';

describe('TauxDImposition Management Update Component', () => {
  let comp: TauxDImpositionUpdateComponent;
  let fixture: ComponentFixture<TauxDImpositionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tauxDImpositionFormService: TauxDImpositionFormService;
  let tauxDImpositionService: TauxDImpositionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TauxDImpositionUpdateComponent],
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
      .overrideTemplate(TauxDImpositionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TauxDImpositionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tauxDImpositionFormService = TestBed.inject(TauxDImpositionFormService);
    tauxDImpositionService = TestBed.inject(TauxDImpositionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tauxDImposition: ITauxDImposition = { id: 456 };

      activatedRoute.data = of({ tauxDImposition });
      comp.ngOnInit();

      expect(comp.tauxDImposition).toEqual(tauxDImposition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITauxDImposition>>();
      const tauxDImposition = { id: 123 };
      jest.spyOn(tauxDImpositionFormService, 'getTauxDImposition').mockReturnValue(tauxDImposition);
      jest.spyOn(tauxDImpositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tauxDImposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tauxDImposition }));
      saveSubject.complete();

      // THEN
      expect(tauxDImpositionFormService.getTauxDImposition).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tauxDImpositionService.update).toHaveBeenCalledWith(expect.objectContaining(tauxDImposition));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITauxDImposition>>();
      const tauxDImposition = { id: 123 };
      jest.spyOn(tauxDImpositionFormService, 'getTauxDImposition').mockReturnValue({ id: null });
      jest.spyOn(tauxDImpositionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tauxDImposition: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tauxDImposition }));
      saveSubject.complete();

      // THEN
      expect(tauxDImpositionFormService.getTauxDImposition).toHaveBeenCalled();
      expect(tauxDImpositionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITauxDImposition>>();
      const tauxDImposition = { id: 123 };
      jest.spyOn(tauxDImpositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tauxDImposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tauxDImpositionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
