import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployeur, NewEmployeur } from '../employeur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeur for edit and NewEmployeurFormGroupInput for create.
 */
type EmployeurFormGroupInput = IEmployeur | PartialWithRequiredKeyOf<NewEmployeur>;

type EmployeurFormDefaults = Pick<NewEmployeur, 'id' | 'conventionCollectives' | 'locations'>;

type EmployeurFormGroupContent = {
  id: FormControl<IEmployeur['id'] | NewEmployeur['id']>;
  name: FormControl<IEmployeur['name']>;
  numeroSiret: FormControl<IEmployeur['numeroSiret']>;
  numApe: FormControl<IEmployeur['numApe']>;
  numUrssaf: FormControl<IEmployeur['numUrssaf']>;
  conventionCollectives: FormControl<IEmployeur['conventionCollectives']>;
  locations: FormControl<IEmployeur['locations']>;
};

export type EmployeurFormGroup = FormGroup<EmployeurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeurFormService {
  createEmployeurFormGroup(employeur: EmployeurFormGroupInput = { id: null }): EmployeurFormGroup {
    const employeurRawValue = {
      ...this.getFormDefaults(),
      ...employeur,
    };
    return new FormGroup<EmployeurFormGroupContent>({
      id: new FormControl(
        { value: employeurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(employeurRawValue.name, {
        validators: [Validators.required],
      }),
      numeroSiret: new FormControl(employeurRawValue.numeroSiret, {
        validators: [Validators.required, Validators.min(14), Validators.max(14)],
      }),
      numApe: new FormControl(employeurRawValue.numApe, {
        validators: [Validators.required, Validators.min(5), Validators.max(5)],
      }),
      numUrssaf: new FormControl(employeurRawValue.numUrssaf, {
        validators: [Validators.required, Validators.min(14), Validators.max(14)],
      }),
      conventionCollectives: new FormControl(employeurRawValue.conventionCollectives ?? []),
      locations: new FormControl(employeurRawValue.locations ?? []),
    });
  }

  getEmployeur(form: EmployeurFormGroup): IEmployeur | NewEmployeur {
    return form.getRawValue() as IEmployeur | NewEmployeur;
  }

  resetForm(form: EmployeurFormGroup, employeur: EmployeurFormGroupInput): void {
    const employeurRawValue = { ...this.getFormDefaults(), ...employeur };
    form.reset(
      {
        ...employeurRawValue,
        id: { value: employeurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeurFormDefaults {
    return {
      id: null,
      conventionCollectives: [],
      locations: [],
    };
  }
}
