import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILocation, NewLocation } from '../location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocation for edit and NewLocationFormGroupInput for create.
 */
type LocationFormGroupInput = ILocation | PartialWithRequiredKeyOf<NewLocation>;

type LocationFormDefaults = Pick<NewLocation, 'id' | 'employees' | 'employeurs'>;

type LocationFormGroupContent = {
  id: FormControl<ILocation['id'] | NewLocation['id']>;
  streetName: FormControl<ILocation['streetName']>;
  numeroRue: FormControl<ILocation['numeroRue']>;
  postalCode: FormControl<ILocation['postalCode']>;
  city: FormControl<ILocation['city']>;
  stateProvince: FormControl<ILocation['stateProvince']>;
  employees: FormControl<ILocation['employees']>;
  employeurs: FormControl<ILocation['employeurs']>;
};

export type LocationFormGroup = FormGroup<LocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocationFormService {
  createLocationFormGroup(location: LocationFormGroupInput = { id: null }): LocationFormGroup {
    const locationRawValue = {
      ...this.getFormDefaults(),
      ...location,
    };
    return new FormGroup<LocationFormGroupContent>({
      id: new FormControl(
        { value: locationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      streetName: new FormControl(locationRawValue.streetName, {
        validators: [Validators.required],
      }),
      numeroRue: new FormControl(locationRawValue.numeroRue, {
        validators: [Validators.required],
      }),
      postalCode: new FormControl(locationRawValue.postalCode, {
        validators: [Validators.required],
      }),
      city: new FormControl(locationRawValue.city, {
        validators: [Validators.required],
      }),
      stateProvince: new FormControl(locationRawValue.stateProvince, {
        validators: [Validators.required],
      }),
      employees: new FormControl(locationRawValue.employees ?? []),
      employeurs: new FormControl(locationRawValue.employeurs ?? []),
    });
  }

  getLocation(form: LocationFormGroup): ILocation | NewLocation {
    return form.getRawValue() as ILocation | NewLocation;
  }

  resetForm(form: LocationFormGroup, location: LocationFormGroupInput): void {
    const locationRawValue = { ...this.getFormDefaults(), ...location };
    form.reset(
      {
        ...locationRawValue,
        id: { value: locationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LocationFormDefaults {
    return {
      id: null,
      employees: [],
      employeurs: [],
    };
  }
}
