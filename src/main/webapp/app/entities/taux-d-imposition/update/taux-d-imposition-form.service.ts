import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITauxDImposition, NewTauxDImposition } from '../taux-d-imposition.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITauxDImposition for edit and NewTauxDImpositionFormGroupInput for create.
 */
type TauxDImpositionFormGroupInput = ITauxDImposition | PartialWithRequiredKeyOf<NewTauxDImposition>;

type TauxDImpositionFormDefaults = Pick<NewTauxDImposition, 'id'>;

type TauxDImpositionFormGroupContent = {
  id: FormControl<ITauxDImposition['id'] | NewTauxDImposition['id']>;
  taux: FormControl<ITauxDImposition['taux']>;
  minSalary: FormControl<ITauxDImposition['minSalary']>;
  maxSalary: FormControl<ITauxDImposition['maxSalary']>;
  startDate: FormControl<ITauxDImposition['startDate']>;
  endDate: FormControl<ITauxDImposition['endDate']>;
};

export type TauxDImpositionFormGroup = FormGroup<TauxDImpositionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TauxDImpositionFormService {
  createTauxDImpositionFormGroup(tauxDImposition: TauxDImpositionFormGroupInput = { id: null }): TauxDImpositionFormGroup {
    const tauxDImpositionRawValue = {
      ...this.getFormDefaults(),
      ...tauxDImposition,
    };
    return new FormGroup<TauxDImpositionFormGroupContent>({
      id: new FormControl(
        { value: tauxDImpositionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      taux: new FormControl(tauxDImpositionRawValue.taux, {
        validators: [Validators.required],
      }),
      minSalary: new FormControl(tauxDImpositionRawValue.minSalary, {
        validators: [Validators.required],
      }),
      maxSalary: new FormControl(tauxDImpositionRawValue.maxSalary),
      startDate: new FormControl(tauxDImpositionRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(tauxDImpositionRawValue.endDate),
    });
  }

  getTauxDImposition(form: TauxDImpositionFormGroup): ITauxDImposition | NewTauxDImposition {
    return form.getRawValue() as ITauxDImposition | NewTauxDImposition;
  }

  resetForm(form: TauxDImpositionFormGroup, tauxDImposition: TauxDImpositionFormGroupInput): void {
    const tauxDImpositionRawValue = { ...this.getFormDefaults(), ...tauxDImposition };
    form.reset(
      {
        ...tauxDImpositionRawValue,
        id: { value: tauxDImpositionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TauxDImpositionFormDefaults {
    return {
      id: null,
    };
  }
}
