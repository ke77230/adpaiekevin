import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CongeFormService } from './conge-form.service';
import { CongeService } from '../service/conge.service';
import { IConge } from '../conge.model';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { ContratService } from 'app/entities/contrat/service/contrat.service';

import { CongeUpdateComponent } from './conge-update.component';

describe('Conge Management Update Component', () => {
  let comp: CongeUpdateComponent;
  let fixture: ComponentFixture<CongeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let congeFormService: CongeFormService;
  let congeService: CongeService;
  let contratService: ContratService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CongeUpdateComponent],
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
      .overrideTemplate(CongeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CongeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    congeFormService = TestBed.inject(CongeFormService);
    congeService = TestBed.inject(CongeService);
    contratService = TestBed.inject(ContratService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contrat query and add missing value', () => {
      const conge: IConge = { id: 456 };
      const contrat: IContrat = { id: 78300 };
      conge.contrat = contrat;

      const contratCollection: IContrat[] = [{ id: 46267 }];
      jest.spyOn(contratService, 'query').mockReturnValue(of(new HttpResponse({ body: contratCollection })));
      const additionalContrats = [contrat];
      const expectedCollection: IContrat[] = [...additionalContrats, ...contratCollection];
      jest.spyOn(contratService, 'addContratToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ conge });
      comp.ngOnInit();

      expect(contratService.query).toHaveBeenCalled();
      expect(contratService.addContratToCollectionIfMissing).toHaveBeenCalledWith(
        contratCollection,
        ...additionalContrats.map(expect.objectContaining)
      );
      expect(comp.contratsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const conge: IConge = { id: 456 };
      const contrat: IContrat = { id: 57423 };
      conge.contrat = contrat;

      activatedRoute.data = of({ conge });
      comp.ngOnInit();

      expect(comp.contratsSharedCollection).toContain(contrat);
      expect(comp.conge).toEqual(conge);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConge>>();
      const conge = { id: 123 };
      jest.spyOn(congeFormService, 'getConge').mockReturnValue(conge);
      jest.spyOn(congeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conge }));
      saveSubject.complete();

      // THEN
      expect(congeFormService.getConge).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(congeService.update).toHaveBeenCalledWith(expect.objectContaining(conge));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConge>>();
      const conge = { id: 123 };
      jest.spyOn(congeFormService, 'getConge').mockReturnValue({ id: null });
      jest.spyOn(congeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conge: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conge }));
      saveSubject.complete();

      // THEN
      expect(congeFormService.getConge).toHaveBeenCalled();
      expect(congeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConge>>();
      const conge = { id: 123 };
      jest.spyOn(congeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(congeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContrat', () => {
      it('Should forward to contratService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contratService, 'compareContrat');
        comp.compareContrat(entity, entity2);
        expect(contratService.compareContrat).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
