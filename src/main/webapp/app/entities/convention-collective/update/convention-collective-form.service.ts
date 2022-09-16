import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConventionCollective, NewConventionCollective } from '../convention-collective.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConventionCollective for edit and NewConventionCollectiveFormGroupInput for create.
 */
type ConventionCollectiveFormGroupInput = IConventionCollective | PartialWithRequiredKeyOf<NewConventionCollective>;

type ConventionCollectiveFormDefaults = Pick<NewConventionCollective, 'id' | 'employeurs'>;

type ConventionCollectiveFormGroupContent = {
  id: FormControl<IConventionCollective['id'] | NewConventionCollective['id']>;
  idcc: FormControl<IConventionCollective['idcc']>;
  nom: FormControl<IConventionCollective['nom']>;
  position: FormControl<IConventionCollective['position']>;
  coefficient: FormControl<IConventionCollective['coefficient']>;
  valeurPoint: FormControl<IConventionCollective['valeurPoint']>;
  baseFixe: FormControl<IConventionCollective['baseFixe']>;
  salaireMinimaux: FormControl<IConventionCollective['salaireMinimaux']>;
  employeurs: FormControl<IConventionCollective['employeurs']>;
};

export type ConventionCollectiveFormGroup = FormGroup<ConventionCollectiveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConventionCollectiveFormService {
  createConventionCollectiveFormGroup(
    conventionCollective: ConventionCollectiveFormGroupInput = { id: null }
  ): ConventionCollectiveFormGroup {
    const conventionCollectiveRawValue = {
      ...this.getFormDefaults(),
      ...conventionCollective,
    };
    return new FormGroup<ConventionCollectiveFormGroupContent>({
      id: new FormControl(
        { value: conventionCollectiveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idcc: new FormControl(conventionCollectiveRawValue.idcc, {
        validators: [Validators.required],
      }),
      nom: new FormControl(conventionCollectiveRawValue.nom, {
        validators: [Validators.required],
      }),
      position: new FormControl(conventionCollectiveRawValue.position, {
        validators: [Validators.required],
      }),
      coefficient: new FormControl(conventionCollectiveRawValue.coefficient, {
        validators: [Validators.required],
      }),
      valeurPoint: new FormControl(conventionCollectiveRawValue.valeurPoint, {
        validators: [Validators.required],
      }),
      baseFixe: new FormControl(conventionCollectiveRawValue.baseFixe, {
        validators: [Validators.required],
      }),
      salaireMinimaux: new FormControl(conventionCollectiveRawValue.salaireMinimaux, {
        validators: [Validators.required],
      }),
      employeurs: new FormControl(conventionCollectiveRawValue.employeurs ?? []),
    });
  }

  getConventionCollective(form: ConventionCollectiveFormGroup): IConventionCollective | NewConventionCollective {
    return form.getRawValue() as IConventionCollective | NewConventionCollective;
  }

  resetForm(form: ConventionCollectiveFormGroup, conventionCollective: ConventionCollectiveFormGroupInput): void {
    const conventionCollectiveRawValue = { ...this.getFormDefaults(), ...conventionCollective };
    form.reset(
      {
        ...conventionCollectiveRawValue,
        id: { value: conventionCollectiveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ConventionCollectiveFormDefaults {
    return {
      id: null,
      employeurs: [],
    };
  }
}
