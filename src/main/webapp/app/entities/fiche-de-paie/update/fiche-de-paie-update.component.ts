import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FicheDePaieFormService, FicheDePaieFormGroup } from './fiche-de-paie-form.service';
import { IFicheDePaie } from '../fiche-de-paie.model';
import { FicheDePaieService } from '../service/fiche-de-paie.service';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { ContratService } from 'app/entities/contrat/service/contrat.service';
import { IEmployeur } from 'app/entities/employeur/employeur.model';
import { EmployeurService } from 'app/entities/employeur/service/employeur.service';
import { ITauxDImposition } from 'app/entities/taux-d-imposition/taux-d-imposition.model';
import { TauxDImpositionService } from 'app/entities/taux-d-imposition/service/taux-d-imposition.service';
import { ICotisation } from 'app/entities/cotisation/cotisation.model';
import { CotisationService } from 'app/entities/cotisation/service/cotisation.service';
import { IMention } from 'app/entities/mention/mention.model';
import { MentionService } from 'app/entities/mention/service/mention.service';

@Component({
  selector: 'jhi-fiche-de-paie-update',
  templateUrl: './fiche-de-paie-update.component.html',
})
export class FicheDePaieUpdateComponent implements OnInit {
  isSaving = false;
  ficheDePaie: IFicheDePaie | null = null;

  contratsSharedCollection: IContrat[] = [];
  employeursSharedCollection: IEmployeur[] = [];
  tauxDImpositionsSharedCollection: ITauxDImposition[] = [];
  cotisationsSharedCollection: ICotisation[] = [];
  mentionsSharedCollection: IMention[] = [];

  editForm: FicheDePaieFormGroup = this.ficheDePaieFormService.createFicheDePaieFormGroup();

  constructor(
    protected ficheDePaieService: FicheDePaieService,
    protected ficheDePaieFormService: FicheDePaieFormService,
    protected contratService: ContratService,
    protected employeurService: EmployeurService,
    protected tauxDImpositionService: TauxDImpositionService,
    protected cotisationService: CotisationService,
    protected mentionService: MentionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareContrat = (o1: IContrat | null, o2: IContrat | null): boolean => this.contratService.compareContrat(o1, o2);

  compareEmployeur = (o1: IEmployeur | null, o2: IEmployeur | null): boolean => this.employeurService.compareEmployeur(o1, o2);

  compareTauxDImposition = (o1: ITauxDImposition | null, o2: ITauxDImposition | null): boolean =>
    this.tauxDImpositionService.compareTauxDImposition(o1, o2);

  compareCotisation = (o1: ICotisation | null, o2: ICotisation | null): boolean => this.cotisationService.compareCotisation(o1, o2);

  compareMention = (o1: IMention | null, o2: IMention | null): boolean => this.mentionService.compareMention(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ficheDePaie }) => {
      this.ficheDePaie = ficheDePaie;
      if (ficheDePaie) {
        this.updateForm(ficheDePaie);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ficheDePaie = this.ficheDePaieFormService.getFicheDePaie(this.editForm);
    if (ficheDePaie.id !== null) {
      this.subscribeToSaveResponse(this.ficheDePaieService.update(ficheDePaie));
    } else {
      this.subscribeToSaveResponse(this.ficheDePaieService.create(ficheDePaie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFicheDePaie>>): void {
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

  protected updateForm(ficheDePaie: IFicheDePaie): void {
    this.ficheDePaie = ficheDePaie;
    this.ficheDePaieFormService.resetForm(this.editForm, ficheDePaie);

    this.contratsSharedCollection = this.contratService.addContratToCollectionIfMissing<IContrat>(
      this.contratsSharedCollection,
      ficheDePaie.contrat
    );
    this.employeursSharedCollection = this.employeurService.addEmployeurToCollectionIfMissing<IEmployeur>(
      this.employeursSharedCollection,
      ficheDePaie.employeur
    );
    this.tauxDImpositionsSharedCollection = this.tauxDImpositionService.addTauxDImpositionToCollectionIfMissing<ITauxDImposition>(
      this.tauxDImpositionsSharedCollection,
      ficheDePaie.imposition
    );
    this.cotisationsSharedCollection = this.cotisationService.addCotisationToCollectionIfMissing<ICotisation>(
      this.cotisationsSharedCollection,
      ...(ficheDePaie.cotisations ?? [])
    );
    this.mentionsSharedCollection = this.mentionService.addMentionToCollectionIfMissing<IMention>(
      this.mentionsSharedCollection,
      ...(ficheDePaie.mentions ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contratService
      .query()
      .pipe(map((res: HttpResponse<IContrat[]>) => res.body ?? []))
      .pipe(
        map((contrats: IContrat[]) => this.contratService.addContratToCollectionIfMissing<IContrat>(contrats, this.ficheDePaie?.contrat))
      )
      .subscribe((contrats: IContrat[]) => (this.contratsSharedCollection = contrats));

    this.employeurService
      .query()
      .pipe(map((res: HttpResponse<IEmployeur[]>) => res.body ?? []))
      .pipe(
        map((employeurs: IEmployeur[]) =>
          this.employeurService.addEmployeurToCollectionIfMissing<IEmployeur>(employeurs, this.ficheDePaie?.employeur)
        )
      )
      .subscribe((employeurs: IEmployeur[]) => (this.employeursSharedCollection = employeurs));

    this.tauxDImpositionService
      .query()
      .pipe(map((res: HttpResponse<ITauxDImposition[]>) => res.body ?? []))
      .pipe(
        map((tauxDImpositions: ITauxDImposition[]) =>
          this.tauxDImpositionService.addTauxDImpositionToCollectionIfMissing<ITauxDImposition>(
            tauxDImpositions,
            this.ficheDePaie?.imposition
          )
        )
      )
      .subscribe((tauxDImpositions: ITauxDImposition[]) => (this.tauxDImpositionsSharedCollection = tauxDImpositions));

    this.cotisationService
      .query()
      .pipe(map((res: HttpResponse<ICotisation[]>) => res.body ?? []))
      .pipe(
        map((cotisations: ICotisation[]) =>
          this.cotisationService.addCotisationToCollectionIfMissing<ICotisation>(cotisations, ...(this.ficheDePaie?.cotisations ?? []))
        )
      )
      .subscribe((cotisations: ICotisation[]) => (this.cotisationsSharedCollection = cotisations));

    this.mentionService
      .query()
      .pipe(map((res: HttpResponse<IMention[]>) => res.body ?? []))
      .pipe(
        map((mentions: IMention[]) =>
          this.mentionService.addMentionToCollectionIfMissing<IMention>(mentions, ...(this.ficheDePaie?.mentions ?? []))
        )
      )
      .subscribe((mentions: IMention[]) => (this.mentionsSharedCollection = mentions));
  }
}
