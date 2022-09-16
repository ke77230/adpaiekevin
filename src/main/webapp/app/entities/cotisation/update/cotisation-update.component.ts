import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CotisationFormService, CotisationFormGroup } from './cotisation-form.service';
import { ICotisation } from '../cotisation.model';
import { CotisationService } from '../service/cotisation.service';
import { Categorie } from 'app/entities/enumerations/categorie.model';

@Component({
  selector: 'jhi-cotisation-update',
  templateUrl: './cotisation-update.component.html',
})
export class CotisationUpdateComponent implements OnInit {
  isSaving = false;
  cotisation: ICotisation | null = null;
  categorieValues = Object.keys(Categorie);

  editForm: CotisationFormGroup = this.cotisationFormService.createCotisationFormGroup();

  constructor(
    protected cotisationService: CotisationService,
    protected cotisationFormService: CotisationFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cotisation }) => {
      this.cotisation = cotisation;
      if (cotisation) {
        this.updateForm(cotisation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cotisation = this.cotisationFormService.getCotisation(this.editForm);
    if (cotisation.id !== null) {
      this.subscribeToSaveResponse(this.cotisationService.update(cotisation));
    } else {
      this.subscribeToSaveResponse(this.cotisationService.create(cotisation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICotisation>>): void {
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

  protected updateForm(cotisation: ICotisation): void {
    this.cotisation = cotisation;
    this.cotisationFormService.resetForm(this.editForm, cotisation);
  }
}
