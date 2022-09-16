import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CongeFormService, CongeFormGroup } from './conge-form.service';
import { IConge } from '../conge.model';
import { CongeService } from '../service/conge.service';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { ContratService } from 'app/entities/contrat/service/contrat.service';
import { Decision } from 'app/entities/enumerations/decision.model';

@Component({
  selector: 'jhi-conge-update',
  templateUrl: './conge-update.component.html',
})
export class CongeUpdateComponent implements OnInit {
  isSaving = false;
  conge: IConge | null = null;
  decisionValues = Object.keys(Decision);

  contratsSharedCollection: IContrat[] = [];

  editForm: CongeFormGroup = this.congeFormService.createCongeFormGroup();

  constructor(
    protected congeService: CongeService,
    protected congeFormService: CongeFormService,
    protected contratService: ContratService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareContrat = (o1: IContrat | null, o2: IContrat | null): boolean => this.contratService.compareContrat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conge }) => {
      this.conge = conge;
      if (conge) {
        this.updateForm(conge);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conge = this.congeFormService.getConge(this.editForm);
    if (conge.id !== null) {
      this.subscribeToSaveResponse(this.congeService.update(conge));
    } else {
      this.subscribeToSaveResponse(this.congeService.create(conge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConge>>): void {
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

  protected updateForm(conge: IConge): void {
    this.conge = conge;
    this.congeFormService.resetForm(this.editForm, conge);

    this.contratsSharedCollection = this.contratService.addContratToCollectionIfMissing<IContrat>(
      this.contratsSharedCollection,
      conge.contrat
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contratService
      .query()
      .pipe(map((res: HttpResponse<IContrat[]>) => res.body ?? []))
      .pipe(map((contrats: IContrat[]) => this.contratService.addContratToCollectionIfMissing<IContrat>(contrats, this.conge?.contrat)))
      .subscribe((contrats: IContrat[]) => (this.contratsSharedCollection = contrats));
  }
}
