import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ContratFormService, ContratFormGroup } from './contrat-form.service';
import { IContrat } from '../contrat.model';
import { ContratService } from '../service/contrat.service';
import { IConventionCollective } from 'app/entities/convention-collective/convention-collective.model';
import { ConventionCollectiveService } from 'app/entities/convention-collective/service/convention-collective.service';
import { IEmployeur } from 'app/entities/employeur/employeur.model';
import { EmployeurService } from 'app/entities/employeur/service/employeur.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { TypeForfait } from 'app/entities/enumerations/type-forfait.model';

@Component({
  selector: 'jhi-contrat-update',
  templateUrl: './contrat-update.component.html',
})
export class ContratUpdateComponent implements OnInit {
  isSaving = false;
  contrat: IContrat | null = null;
  typeForfaitValues = Object.keys(TypeForfait);

  conventionCollectivesSharedCollection: IConventionCollective[] = [];
  employeursSharedCollection: IEmployeur[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm: ContratFormGroup = this.contratFormService.createContratFormGroup();

  constructor(
    protected contratService: ContratService,
    protected contratFormService: ContratFormService,
    protected conventionCollectiveService: ConventionCollectiveService,
    protected employeurService: EmployeurService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareConventionCollective = (o1: IConventionCollective | null, o2: IConventionCollective | null): boolean =>
    this.conventionCollectiveService.compareConventionCollective(o1, o2);

  compareEmployeur = (o1: IEmployeur | null, o2: IEmployeur | null): boolean => this.employeurService.compareEmployeur(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contrat }) => {
      this.contrat = contrat;
      if (contrat) {
        this.updateForm(contrat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contrat = this.contratFormService.getContrat(this.editForm);
    if (contrat.id !== null) {
      this.subscribeToSaveResponse(this.contratService.update(contrat));
    } else {
      this.subscribeToSaveResponse(this.contratService.create(contrat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContrat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contrat: IContrat): void {
    this.contrat = contrat;
    this.contratFormService.resetForm(this.editForm, contrat);

    this.conventionCollectivesSharedCollection =
      this.conventionCollectiveService.addConventionCollectiveToCollectionIfMissing<IConventionCollective>(
        this.conventionCollectivesSharedCollection,
        contrat.conventionCollective
      );
    this.employeursSharedCollection = this.employeurService.addEmployeurToCollectionIfMissing<IEmployeur>(
      this.employeursSharedCollection,
      contrat.employeur
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      contrat.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.conventionCollectiveService
      .query()
      .pipe(map((res: HttpResponse<IConventionCollective[]>) => res.body ?? []))
      .pipe(
        map((conventionCollectives: IConventionCollective[]) =>
          this.conventionCollectiveService.addConventionCollectiveToCollectionIfMissing<IConventionCollective>(
            conventionCollectives,
            this.contrat?.conventionCollective
          )
        )
      )
      .subscribe((conventionCollectives: IConventionCollective[]) => (this.conventionCollectivesSharedCollection = conventionCollectives));

    this.employeurService
      .query()
      .pipe(map((res: HttpResponse<IEmployeur[]>) => res.body ?? []))
      .pipe(
        map((employeurs: IEmployeur[]) =>
          this.employeurService.addEmployeurToCollectionIfMissing<IEmployeur>(employeurs, this.contrat?.employeur)
        )
      )
      .subscribe((employeurs: IEmployeur[]) => (this.employeursSharedCollection = employeurs));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) => this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.contrat?.employee))
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
