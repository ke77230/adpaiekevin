import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContrat, NewContrat } from '../contrat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContrat for edit and NewContratFormGroupInput for create.
 */
type ContratFormGroupInput = IContrat | PartialWithRequiredKeyOf<NewContrat>;

type ContratFormDefaults = Pick<NewContrat, 'id'>;

type ContratFormGroupContent = {
  id: FormControl<IContrat['id'] | NewContrat['id']>;
  salaireBase: FormControl<IContrat['salaireBase']>;
  emploi: FormControl<IContrat['emploi']>;
  dateArrive: FormControl<IContrat['dateArrive']>;
  classification: FormControl<IContrat['classification']>;
  typeForfait: FormControl<IContrat['typeForfait']>;
  nbHeure: FormControl<IContrat['nbHeure']>;
  conventionCollective: FormControl<IContrat['conventionCollective']>;
  employeur: FormControl<IContrat['employeur']>;
  employee: FormControl<IContrat['employee']>;
};

export type ContratFormGroup = FormGroup<ContratFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContratFormService {
  createContratFormGroup(contrat: ContratFormGroupInput = { id: null }): ContratFormGroup {
    const contratRawValue = {
      ...this.getFormDefaults(),
      ...contrat,
    };
    return new FormGroup<ContratFormGroupContent>({
      id: new FormControl(
        { value: contratRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      salaireBase: new FormControl(contratRawValue.salaireBase, {
        validators: [Validators.required],
      }),
      emploi: new FormControl(contratRawValue.emploi, {
        validators: [Validators.required],
      }),
      dateArrive: new FormControl(contratRawValue.dateArrive, {
        validators: [Validators.required],
      }),
      classification: new FormControl(contratRawValue.classification, {
        validators: [Validators.required],
      }),
      typeForfait: new FormControl(contratRawValue.typeForfait, {
        validators: [Validators.required],
      }),
      nbHeure: new FormControl(contratRawValue.nbHeure),
      conventionCollective: new FormControl(contratRawValue.conventionCollective),
      employeur: new FormControl(contratRawValue.employeur),
      employee: new FormControl(contratRawValue.employee),
    });
  }

  getContrat(form: ContratFormGroup): IContrat | NewContrat {
    return form.getRawValue() as IContrat | NewContrat;
  }

  resetForm(form: ContratFormGroup, contrat: ContratFormGroupInput): void {
    const contratRawValue = { ...this.getFormDefaults(), ...contrat };
    form.reset(
      {
        ...contratRawValue,
        id: { value: contratRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContratFormDefaults {
    return {
      id: null,
    };
  }
}
