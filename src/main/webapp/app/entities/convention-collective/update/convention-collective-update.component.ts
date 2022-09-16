import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ConventionCollectiveFormService, ConventionCollectiveFormGroup } from './convention-collective-form.service';
import { IConventionCollective } from '../convention-collective.model';
import { ConventionCollectiveService } from '../service/convention-collective.service';

@Component({
  selector: 'jhi-convention-collective-update',
  templateUrl: './convention-collective-update.component.html',
})
export class ConventionCollectiveUpdateComponent implements OnInit {
  isSaving = false;
  conventionCollective: IConventionCollective | null = null;

  editForm: ConventionCollectiveFormGroup = this.conventionCollectiveFormService.createConventionCollectiveFormGroup();

  constructor(
    protected conventionCollectiveService: ConventionCollectiveService,
    protected conventionCollectiveFormService: ConventionCollectiveFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conventionCollective }) => {
      this.conventionCollective = conventionCollective;
      if (conventionCollective) {
        this.updateForm(conventionCollective);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conventionCollective = this.conventionCollectiveFormService.getConventionCollective(this.editForm);
    if (conventionCollective.id !== null) {
      this.subscribeToSaveResponse(this.conventionCollectiveService.update(conventionCollective));
    } else {
      this.subscribeToSaveResponse(this.conventionCollectiveService.create(conventionCollective));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConventionCollective>>): void {
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

  protected updateForm(conventionCollective: IConventionCollective): void {
    this.conventionCollective = conventionCollective;
    this.conventionCollectiveFormService.resetForm(this.editForm, conventionCollective);
  }
}
