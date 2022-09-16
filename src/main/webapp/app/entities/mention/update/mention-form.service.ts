import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMention, NewMention } from '../mention.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMention for edit and NewMentionFormGroupInput for create.
 */
type MentionFormGroupInput = IMention | PartialWithRequiredKeyOf<NewMention>;

type MentionFormDefaults = Pick<NewMention, 'id' | 'ficheDePaies'>;

type MentionFormGroupContent = {
  id: FormControl<IMention['id'] | NewMention['id']>;
  mention: FormControl<IMention['mention']>;
  ficheDePaies: FormControl<IMention['ficheDePaies']>;
};

export type MentionFormGroup = FormGroup<MentionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MentionFormService {
  createMentionFormGroup(mention: MentionFormGroupInput = { id: null }): MentionFormGroup {
    const mentionRawValue = {
      ...this.getFormDefaults(),
      ...mention,
    };
    return new FormGroup<MentionFormGroupContent>({
      id: new FormControl(
        { value: mentionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      mention: new FormControl(mentionRawValue.mention, {
        validators: [Validators.required],
      }),
      ficheDePaies: new FormControl(mentionRawValue.ficheDePaies ?? []),
    });
  }

  getMention(form: MentionFormGroup): IMention | NewMention {
    return form.getRawValue() as IMention | NewMention;
  }

  resetForm(form: MentionFormGroup, mention: MentionFormGroupInput): void {
    const mentionRawValue = { ...this.getFormDefaults(), ...mention };
    form.reset(
      {
        ...mentionRawValue,
        id: { value: mentionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MentionFormDefaults {
    return {
      id: null,
      ficheDePaies: [],
    };
  }
}
