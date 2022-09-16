import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TauxDImpositionFormService, TauxDImpositionFormGroup } from './taux-d-imposition-form.service';
import { ITauxDImposition } from '../taux-d-imposition.model';
import { TauxDImpositionService } from '../service/taux-d-imposition.service';

@Component({
  selector: 'jhi-taux-d-imposition-update',
  templateUrl: './taux-d-imposition-update.component.html',
})
export class TauxDImpositionUpdateComponent implements OnInit {
  isSaving = false;
  tauxDImposition: ITauxDImposition | null = null;

  editForm: TauxDImpositionFormGroup = this.tauxDImpositionFormService.createTauxDImpositionFormGroup();

  constructor(
    protected tauxDImpositionService: TauxDImpositionService,
    protected tauxDImpositionFormService: TauxDImpositionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tauxDImposition }) => {
      this.tauxDImposition = tauxDImposition;
      if (tauxDImposition) {
        this.updateForm(tauxDImposition);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tauxDImposition = this.tauxDImpositionFormService.getTauxDImposition(this.editForm);
    if (tauxDImposition.id !== null) {
      this.subscribeToSaveResponse(this.tauxDImpositionService.update(tauxDImposition));
    } else {
      this.subscribeToSaveResponse(this.tauxDImpositionService.create(tauxDImposition));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITauxDImposition>>): void {
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

  protected updateForm(tauxDImposition: ITauxDImposition): void {
    this.tauxDImposition = tauxDImposition;
    this.tauxDImpositionFormService.resetForm(this.editForm, tauxDImposition);
  }
}
