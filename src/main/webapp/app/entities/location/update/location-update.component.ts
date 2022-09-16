import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LocationFormService, LocationFormGroup } from './location-form.service';
import { ILocation } from '../location.model';
import { LocationService } from '../service/location.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IEmployeur } from 'app/entities/employeur/employeur.model';
import { EmployeurService } from 'app/entities/employeur/service/employeur.service';

@Component({
  selector: 'jhi-location-update',
  templateUrl: './location-update.component.html',
})
export class LocationUpdateComponent implements OnInit {
  isSaving = false;
  location: ILocation | null = null;

  employeesSharedCollection: IEmployee[] = [];
  employeursSharedCollection: IEmployeur[] = [];

  editForm: LocationFormGroup = this.locationFormService.createLocationFormGroup();

  constructor(
    protected locationService: LocationService,
    protected locationFormService: LocationFormService,
    protected employeeService: EmployeeService,
    protected employeurService: EmployeurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareEmployeur = (o1: IEmployeur | null, o2: IEmployeur | null): boolean => this.employeurService.compareEmployeur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ location }) => {
      this.location = location;
      if (location) {
        this.updateForm(location);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const location = this.locationFormService.getLocation(this.editForm);
    if (location.id !== null) {
      this.subscribeToSaveResponse(this.locationService.update(location));
    } else {
      this.subscribeToSaveResponse(this.locationService.create(location));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>): void {
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

  protected updateForm(location: ILocation): void {
    this.location = location;
    this.locationFormService.resetForm(this.editForm, location);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      ...(location.employees ?? [])
    );
    this.employeursSharedCollection = this.employeurService.addEmployeurToCollectionIfMissing<IEmployeur>(
      this.employeursSharedCollection,
      ...(location.employeurs ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, ...(this.location?.employees ?? []))
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.employeurService
      .query()
      .pipe(map((res: HttpResponse<IEmployeur[]>) => res.body ?? []))
      .pipe(
        map((employeurs: IEmployeur[]) =>
          this.employeurService.addEmployeurToCollectionIfMissing<IEmployeur>(employeurs, ...(this.location?.employeurs ?? []))
        )
      )
      .subscribe((employeurs: IEmployeur[]) => (this.employeursSharedCollection = employeurs));
  }
}
