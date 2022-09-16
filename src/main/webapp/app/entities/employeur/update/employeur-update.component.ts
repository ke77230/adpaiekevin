import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeurFormService, EmployeurFormGroup } from './employeur-form.service';
import { IEmployeur } from '../employeur.model';
import { EmployeurService } from '../service/employeur.service';
import { IConventionCollective } from 'app/entities/convention-collective/convention-collective.model';
import { ConventionCollectiveService } from 'app/entities/convention-collective/service/convention-collective.service';

@Component({
  selector: 'jhi-employeur-update',
  templateUrl: './employeur-update.component.html',
})
export class EmployeurUpdateComponent implements OnInit {
  isSaving = false;
  employeur: IEmployeur | null = null;

  conventionCollectivesSharedCollection: IConventionCollective[] = [];

  editForm: EmployeurFormGroup = this.employeurFormService.createEmployeurFormGroup();

  constructor(
    protected employeurService: EmployeurService,
    protected employeurFormService: EmployeurFormService,
    protected conventionCollectiveService: ConventionCollectiveService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareConventionCollective = (o1: IConventionCollective | null, o2: IConventionCollective | null): boolean =>
    this.conventionCollectiveService.compareConventionCollective(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeur }) => {
      this.employeur = employeur;
      if (employeur) {
        this.updateForm(employeur);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeur = this.employeurFormService.getEmployeur(this.editForm);
    if (employeur.id !== null) {
      this.subscribeToSaveResponse(this.employeurService.update(employeur));
    } else {
      this.subscribeToSaveResponse(this.employeurService.create(employeur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeur>>): void {
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

  protected updateForm(employeur: IEmployeur): void {
    this.employeur = employeur;
    this.employeurFormService.resetForm(this.editForm, employeur);

    this.conventionCollectivesSharedCollection =
      this.conventionCollectiveService.addConventionCollectiveToCollectionIfMissing<IConventionCollective>(
        this.conventionCollectivesSharedCollection,
        ...(employeur.conventionCollectives ?? [])
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
            ...(this.employeur?.conventionCollectives ?? [])
          )
        )
      )
      .subscribe((conventionCollectives: IConventionCollective[]) => (this.conventionCollectivesSharedCollection = conventionCollectives));
  }
}
