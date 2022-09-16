import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICotisation, NewCotisation } from '../cotisation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICotisation for edit and NewCotisationFormGroupInput for create.
 */
type CotisationFormGroupInput = ICotisation | PartialWithRequiredKeyOf<NewCotisation>;

type CotisationFormDefaults = Pick<NewCotisation, 'id' | 'actuel' | 'ficheDePaies'>;

type CotisationFormGroupContent = {
  id: FormControl<ICotisation['id'] | NewCotisation['id']>;
  name: FormControl<ICotisation['name']>;
  famille: FormControl<ICotisation['famille']>;
  taux: FormControl<ICotisation['taux']>;
  startDate: FormControl<ICotisation['startDate']>;
  endDate: FormControl<ICotisation['endDate']>;
  actuel: FormControl<ICotisation['actuel']>;
  ficheDePaies: FormControl<ICotisation['ficheDePaies']>;
};

export type CotisationFormGroup = FormGroup<CotisationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CotisationFormService {
  createCotisationFormGroup(cotisation: CotisationFormGroupInput = { id: null }): CotisationFormGroup {
    const cotisationRawValue = {
      ...this.getFormDefaults(),
      ...cotisation,
    };
    return new FormGroup<CotisationFormGroupContent>({
      id: new FormControl(
        { value: cotisationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(cotisationRawValue.name, {
        validators: [Validators.required],
      }),
      famille: new FormControl(cotisationRawValue.famille, {
        validators: [Validators.required],
      }),
      taux: new FormControl(cotisationRawValue.taux, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(cotisationRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(cotisationRawValue.endDate),
      actuel: new FormControl(cotisationRawValue.actuel, {
        validators: [Validators.required],
      }),
      ficheDePaies: new FormControl(cotisationRawValue.ficheDePaies ?? []),
    });
  }

  getCotisation(form: CotisationFormGroup): ICotisation | NewCotisation {
    return form.getRawValue() as ICotisation | NewCotisation;
  }

  resetForm(form: CotisationFormGroup, cotisation: CotisationFormGroupInput): void {
    const cotisationRawValue = { ...this.getFormDefaults(), ...cotisation };
    form.reset(
      {
        ...cotisationRawValue,
        id: { value: cotisationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CotisationFormDefaults {
    return {
      id: null,
      actuel: false,
      ficheDePaies: [],
    };
  }
}
