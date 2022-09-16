import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContratFormService } from './contrat-form.service';
import { ContratService } from '../service/contrat.service';
import { IContrat } from '../contrat.model';
import { IConventionCollective } from 'app/entities/convention-collective/convention-collective.model';
import { ConventionCollectiveService } from 'app/entities/convention-collective/service/convention-collective.service';
import { IEmployeur } from 'app/entities/employeur/employeur.model';
import { EmployeurService } from 'app/entities/employeur/service/employeur.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { ContratUpdateComponent } from './contrat-update.component';

describe('Contrat Management Update Component', () => {
  let comp: ContratUpdateComponent;
  let fixture: ComponentFixture<ContratUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contratFormService: ContratFormService;
  let contratService: ContratService;
  let conventionCollectiveService: ConventionCollectiveService;
  let employeurService: EmployeurService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContratUpdateComponent],
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
      .overrideTemplate(ContratUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContratUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contratFormService = TestBed.inject(ContratFormService);
    contratService = TestBed.inject(ContratService);
    conventionCollectiveService = TestBed.inject(ConventionCollectiveService);
    employeurService = TestBed.inject(EmployeurService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ConventionCollective query and add missing value', () => {
      const contrat: IContrat = { id: 456 };
      const conventionCollective: IConventionCollective = { id: 17679 };
      contrat.conventionCollective = conventionCollective;

      const conventionCollectiveCollection: IConventionCollective[] = [{ id: 83786 }];
      jest.spyOn(conventionCollectiveService, 'query').mockReturnValue(of(new HttpResponse({ body: conventionCollectiveCollection })));
      const additionalConventionCollectives = [conventionCollective];
      const expectedCollection: IConventionCollective[] = [...additionalConventionCollectives, ...conventionCollectiveCollection];
      jest.spyOn(conventionCollectiveService, 'addConventionCollectiveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      expect(conventionCollectiveService.query).toHaveBeenCalled();
      expect(conventionCollectiveService.addConventionCollectiveToCollectionIfMissing).toHaveBeenCalledWith(
        conventionCollectiveCollection,
        ...additionalConventionCollectives.map(expect.objectContaining)
      );
      expect(comp.conventionCollectivesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employeur query and add missing value', () => {
      const contrat: IContrat = { id: 456 };
      const employeur: IEmployeur = { id: 84769 };
      contrat.employeur = employeur;

      const employeurCollection: IEmployeur[] = [{ id: 73046 }];
      jest.spyOn(employeurService, 'query').mockReturnValue(of(new HttpResponse({ body: employeurCollection })));
      const additionalEmployeurs = [employeur];
      const expectedCollection: IEmployeur[] = [...additionalEmployeurs, ...employeurCollection];
      jest.spyOn(employeurService, 'addEmployeurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      expect(employeurService.query).toHaveBeenCalled();
      expect(employeurService.addEmployeurToCollectionIfMissing).toHaveBeenCalledWith(
        employeurCollection,
        ...additionalEmployeurs.map(expect.objectContaining)
      );
      expect(comp.employeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const contrat: IContrat = { id: 456 };
      const employee: IEmployee = { id: 78969 };
      contrat.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 75945 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contrat: IContrat = { id: 456 };
      const conventionCollective: IConventionCollective = { id: 56109 };
      contrat.conventionCollective = conventionCollective;
      const employeur: IEmployeur = { id: 34051 };
      contrat.employeur = employeur;
      const employee: IEmployee = { id: 30285 };
      contrat.employee = employee;

      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      expect(comp.conventionCollectivesSharedCollection).toContain(conventionCollective);
      expect(comp.employeursSharedCollection).toContain(employeur);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.contrat).toEqual(contrat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContrat>>();
      const contrat = { id: 123 };
      jest.spyOn(contratFormService, 'getContrat').mockReturnValue(contrat);
      jest.spyOn(contratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contrat }));
      saveSubject.complete();

      // THEN
      expect(contratFormService.getContrat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contratService.update).toHaveBeenCalledWith(expect.objectContaining(contrat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContrat>>();
      const contrat = { id: 123 };
      jest.spyOn(contratFormService, 'getContrat').mockReturnValue({ id: null });
      jest.spyOn(contratService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contrat }));
      saveSubject.complete();

      // THEN
      expect(contratFormService.getContrat).toHaveBeenCalled();
      expect(contratService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContrat>>();
      const contrat = { id: 123 };
      jest.spyOn(contratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contratService.update).toHaveBeenCalled();
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

    describe('compareEmployeur', () => {
      it('Should forward to employeurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeurService, 'compareEmployeur');
        comp.compareEmployeur(entity, entity2);
        expect(employeurService.compareEmployeur).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
