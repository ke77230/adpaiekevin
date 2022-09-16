import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeurFormService } from './employeur-form.service';
import { EmployeurService } from '../service/employeur.service';
import { IEmployeur } from '../employeur.model';
import { IConventionCollective } from 'app/entities/convention-collective/convention-collective.model';
import { ConventionCollectiveService } from 'app/entities/convention-collective/service/convention-collective.service';

import { EmployeurUpdateComponent } from './employeur-update.component';

describe('Employeur Management Update Component', () => {
  let comp: EmployeurUpdateComponent;
  let fixture: ComponentFixture<EmployeurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeurFormService: EmployeurFormService;
  let employeurService: EmployeurService;
  let conventionCollectiveService: ConventionCollectiveService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeurUpdateComponent],
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
      .overrideTemplate(EmployeurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeurFormService = TestBed.inject(EmployeurFormService);
    employeurService = TestBed.inject(EmployeurService);
    conventionCollectiveService = TestBed.inject(ConventionCollectiveService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ConventionCollective query and add missing value', () => {
      const employeur: IEmployeur = { id: 456 };
      const conventionCollectives: IConventionCollective[] = [{ id: 3310 }];
      employeur.conventionCollectives = conventionCollectives;

      const conventionCollectiveCollection: IConventionCollective[] = [{ id: 2186 }];
      jest.spyOn(conventionCollectiveService, 'query').mockReturnValue(of(new HttpResponse({ body: conventionCollectiveCollection })));
      const additionalConventionCollectives = [...conventionCollectives];
      const expectedCollection: IConventionCollective[] = [...additionalConventionCollectives, ...conventionCollectiveCollection];
      jest.spyOn(conventionCollectiveService, 'addConventionCollectiveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeur });
      comp.ngOnInit();

      expect(conventionCollectiveService.query).toHaveBeenCalled();
      expect(conventionCollectiveService.addConventionCollectiveToCollectionIfMissing).toHaveBeenCalledWith(
        conventionCollectiveCollection,
        ...additionalConventionCollectives.map(expect.objectContaining)
      );
      expect(comp.conventionCollectivesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeur: IEmployeur = { id: 456 };
      const conventionCollective: IConventionCollective = { id: 56446 };
      employeur.conventionCollectives = [conventionCollective];

      activatedRoute.data = of({ employeur });
      comp.ngOnInit();

      expect(comp.conventionCollectivesSharedCollection).toContain(conventionCollective);
      expect(comp.employeur).toEqual(employeur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeur>>();
      const employeur = { id: 123 };
      jest.spyOn(employeurFormService, 'getEmployeur').mockReturnValue(employeur);
      jest.spyOn(employeurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeur }));
      saveSubject.complete();

      // THEN
      expect(employeurFormService.getEmployeur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeurService.update).toHaveBeenCalledWith(expect.objectContaining(employeur));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeur>>();
      const employeur = { id: 123 };
      jest.spyOn(employeurFormService, 'getEmployeur').mockReturnValue({ id: null });
      jest.spyOn(employeurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeur }));
      saveSubject.complete();

      // THEN
      expect(employeurFormService.getEmployeur).toHaveBeenCalled();
      expect(employeurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeur>>();
      const employeur = { id: 123 };
      jest.spyOn(employeurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareConventionCollective', () => {
      it('Should forward to conventionCollectiveService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(conventionCollectiveService, 'compareConventionCollective');
        comp.compareConventionCollective(entity, entity2);
        expect(conventionCollectiveService.compareConventionCollective).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
