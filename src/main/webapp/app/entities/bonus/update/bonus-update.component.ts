import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BonusFormService, BonusFormGroup } from './bonus-form.service';
import { IBonus } from '../bonus.model';
import { BonusService } from '../service/bonus.service';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { ContratService } from 'app/entities/contrat/service/contrat.service';

@Component({
  selector: 'jhi-bonus-update',
  templateUrl: './bonus-update.component.html',
})
export class BonusUpdateComponent implements OnInit {
  isSaving = false;
  bonus: IBonus | null = null;

  contratsSharedCollection: IContrat[] = [];

  editForm: BonusFormGroup = this.bonusFormService.createBonusFormGroup();

  constructor(
    protected bonusService: BonusService,
    protected bonusFormService: BonusFormService,
    protected contratService: ContratService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareContrat = (o1: IContrat | null, o2: IContrat | null): boolean => this.contratService.compareContrat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bonus }) => {
      this.bonus = bonus;
      if (bonus) {
        this.updateForm(bonus);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bonus = this.bonusFormService.getBonus(this.editForm);
    if (bonus.id !== null) {
      this.subscribeToSaveResponse(this.bonusService.update(bonus));
    } else {
      this.subscribeToSaveResponse(this.bonusService.create(bonus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBonus>>): void {
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

  protected updateForm(bonus: IBonus): void {
    this.bonus = bonus;
    this.bonusFormService.resetForm(this.editForm, bonus);

    this.contratsSharedCollection = this.contratService.addContratToCollectionIfMissing<IContrat>(
      this.contratsSharedCollection,
      bonus.contrat
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contratService
      .query()
      .pipe(map((res: HttpResponse<IContrat[]>) => res.body ?? []))
      .pipe(map((contrats: IContrat[]) => this.contratService.addContratToCollectionIfMissing<IContrat>(contrats, this.bonus?.contrat)))
      .subscribe((contrats: IContrat[]) => (this.contratsSharedCollection = contrats));
  }
}
