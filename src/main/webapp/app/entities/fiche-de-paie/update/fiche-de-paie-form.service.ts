import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFicheDePaie, NewFicheDePaie } from '../fiche-de-paie.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFicheDePaie for edit and NewFicheDePaieFormGroupInput for create.
 */
type FicheDePaieFormGroupInput = IFicheDePaie | PartialWithRequiredKeyOf<NewFicheDePaie>;

type FicheDePaieFormDefaults = Pick<NewFicheDePaie, 'id' | 'cotisations' | 'mentions'>;

type FicheDePaieFormGroupContent = {
  id: FormControl<IFicheDePaie['id'] | NewFicheDePaie['id']>;
  salaireBrut: FormControl<IFicheDePaie['salaireBrut']>;
  startDate: FormControl<IFicheDePaie['startDate']>;
  endDate: FormControl<IFicheDePaie['endDate']>;
  datepaiement: FormControl<IFicheDePaie['datepaiement']>;
  salaireNet: FormControl<IFicheDePaie['salaireNet']>;
  montantNetAvantImpots: FormControl<IFicheDePaie['montantNetAvantImpots']>;
  proFees: FormControl<IFicheDePaie['proFees']>;
  deductions: FormControl<IFicheDePaie['deductions']>;
  contrat: FormControl<IFicheDePaie['contrat']>;
  employeur: FormControl<IFicheDePaie['employeur']>;
  imposition: FormControl<IFicheDePaie['imposition']>;
  cotisations: FormControl<IFicheDePaie['cotisations']>;
  mentions: FormControl<IFicheDePaie['mentions']>;
};

export type FicheDePaieFormGroup = FormGroup<FicheDePaieFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FicheDePaieFormService {
  createFicheDePaieFormGroup(ficheDePaie: FicheDePaieFormGroupInput = { id: null }): FicheDePaieFormGroup {
    const ficheDePaieRawValue = {
      ...this.getFormDefaults(),
      ...ficheDePaie,
    };
    return new FormGroup<FicheDePaieFormGroupContent>({
      id: new FormControl(
        { value: ficheDePaieRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      salaireBrut: new FormControl(ficheDePaieRawValue.salaireBrut, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(ficheDePaieRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(ficheDePaieRawValue.endDate, {
        validators: [Validators.required],
      }),
      datepaiement: new FormControl(ficheDePaieRawValue.datepaiement, {
        validators: [Validators.required],
      }),
      salaireNet: new FormControl(ficheDePaieRawValue.salaireNet, {
        validators: [Validators.required],
      }),
      montantNetAvantImpots: new FormControl(ficheDePaieRawValue.montantNetAvantImpots, {
        validators: [Validators.required],
      }),
      proFees: new FormControl(ficheDePaieRawValue.proFees, {
        validators: [Validators.required],
      }),
      deductions: new FormControl(ficheDePaieRawValue.deductions),
      contrat: new FormControl(ficheDePaieRawValue.contrat),
      employeur: new FormControl(ficheDePaieRawValue.employeur),
      imposition: new FormControl(ficheDePaieRawValue.imposition),
      cotisations: new FormControl(ficheDePaieRawValue.cotisations ?? []),
      mentions: new FormControl(ficheDePaieRawValue.mentions ?? []),
    });
  }

  getFicheDePaie(form: FicheDePaieFormGroup): IFicheDePaie | NewFicheDePaie {
    return form.getRawValue() as IFicheDePaie | NewFicheDePaie;
  }

  resetForm(form: FicheDePaieFormGroup, ficheDePaie: FicheDePaieFormGroupInput): void {
    const ficheDePaieRawValue = { ...this.getFormDefaults(), ...ficheDePaie };
    form.reset(
      {
        ...ficheDePaieRawValue,
        id: { value: ficheDePaieRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FicheDePaieFormDefaults {
    return {
      id: null,
      cotisations: [],
      mentions: [],
    };
  }
}
