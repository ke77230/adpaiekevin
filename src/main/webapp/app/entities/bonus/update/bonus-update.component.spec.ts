import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BonusFormService } from './bonus-form.service';
import { BonusService } from '../service/bonus.service';
import { IBonus } from '../bonus.model';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { ContratService } from 'app/entities/contrat/service/contrat.service';

import { BonusUpdateComponent } from './bonus-update.component';

describe('Bonus Management Update Component', () => {
  let comp: BonusUpdateComponent;
  let fixture: ComponentFixture<BonusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bonusFormService: BonusFormService;
  let bonusService: BonusService;
  let contratService: ContratService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BonusUpdateComponent],
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
      .overrideTemplate(BonusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BonusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bonusFormService = TestBed.inject(BonusFormService);
    bonusService = TestBed.inject(BonusService);
    contratService = TestBed.inject(ContratService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contrat query and add missing value', () => {
      const bonus: IBonus = { id: 456 };
      const contrat: IContrat = { id: 71940 };
      bonus.contrat = contrat;

      const contratCollection: IContrat[] = [{ id: 26334 }];
      jest.spyOn(contratService, 'query').mockReturnValue(of(new HttpResponse({ body: contratCollection })));
      const additionalContrats = [contrat];
      const expectedCollection: IContrat[] = [...additionalContrats, ...contratCollection];
      jest.spyOn(contratService, 'addContratToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bonus });
      comp.ngOnInit();

      expect(contratService.query).toHaveBeenCalled();
      expect(contratService.addContratToCollectionIfMissing).toHaveBeenCalledWith(
        contratCollection,
        ...additionalContrats.map(expect.objectContaining)
      );
      expect(comp.contratsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bonus: IBonus = { id: 456 };
      const contrat: IContrat = { id: 17361 };
      bonus.contrat = contrat;

      activatedRoute.data = of({ bonus });
      comp.ngOnInit();

      expect(comp.contratsSharedCollection).toContain(contrat);
      expect(comp.bonus).toEqual(bonus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBonus>>();
      const bonus = { id: 123 };
      jest.spyOn(bonusFormService, 'getBonus').mockReturnValue(bonus);
      jest.spyOn(bonusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bonus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bonus }));
      saveSubject.complete();

      // THEN
      expect(bonusFormService.getBonus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bonusService.update).toHaveBeenCalledWith(expect.objectContaining(bonus));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBonus>>();
      const bonus = { id: 123 };
      jest.spyOn(bonusFormService, 'getBonus').mockReturnValue({ id: null });
      jest.spyOn(bonusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bonus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bonus }));
      saveSubject.complete();

      // THEN
      expect(bonusFormService.getBonus).toHaveBeenCalled();
      expect(bonusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBonus>>();
      const bonus = { id: 123 };
      jest.spyOn(bonusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bonus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bonusService.update).toHaveBeenCalled();
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
