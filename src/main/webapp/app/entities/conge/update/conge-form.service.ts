import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConge, NewConge } from '../conge.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConge for edit and NewCongeFormGroupInput for create.
 */
type CongeFormGroupInput = IConge | PartialWithRequiredKeyOf<NewConge>;

type CongeFormDefaults = Pick<NewConge, 'id'>;

type CongeFormGroupContent = {
  id: FormControl<IConge['id'] | NewConge['id']>;
  holdateStart: FormControl<IConge['holdateStart']>;
  holdateEnd: FormControl<IConge['holdateEnd']>;
  holdatePay: FormControl<IConge['holdatePay']>;
  nbCongeAcquis: FormControl<IConge['nbCongeAcquis']>;
  nbCongePris: FormControl<IConge['nbCongePris']>;
  dateDemande: FormControl<IConge['dateDemande']>;
  decision: FormControl<IConge['decision']>;
  dateReponse: FormControl<IConge['dateReponse']>;
  contrat: FormControl<IConge['contrat']>;
};

export type CongeFormGroup = FormGroup<CongeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CongeFormService {
  createCongeFormGroup(conge: CongeFormGroupInput = { id: null }): CongeFormGroup {
    const congeRawValue = {
      ...this.getFormDefaults(),
      ...conge,
    };
    return new FormGroup<CongeFormGroupContent>({
      id: new FormControl(
        { value: congeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      holdateStart: new FormControl(congeRawValue.holdateStart, {
        validators: [Validators.required],
      }),
      holdateEnd: new FormControl(congeRawValue.holdateEnd, {
        validators: [Validators.required],
      }),
      holdatePay: new FormControl(congeRawValue.holdatePay, {
        validators: [Validators.required],
      }),
      nbCongeAcquis: new FormControl(congeRawValue.nbCongeAcquis),
      nbCongePris: new FormControl(congeRawValue.nbCongePris, {
        validators: [Validators.required],
      }),
      dateDemande: new FormControl(congeRawValue.dateDemande, {
        validators: [Validators.required],
      }),
      decision: new FormControl(congeRawValue.decision, {
        validators: [Validators.required],
      }),
      dateReponse: new FormControl(congeRawValue.dateReponse),
      contrat: new FormControl(congeRawValue.contrat),
    });
  }

  getConge(form: CongeFormGroup): IConge | NewConge {
    return form.getRawValue() as IConge | NewConge;
  }

  resetForm(form: CongeFormGroup, conge: CongeFormGroupInput): void {
    const congeRawValue = { ...this.getFormDefaults(), ...conge };
    form.reset(
      {
        ...congeRawValue,
        id: { value: congeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CongeFormDefaults {
    return {
      id: null,
    };
  }
}
