import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../bonus.test-samples';

import { BonusFormService } from './bonus-form.service';

describe('Bonus Form Service', () => {
  let service: BonusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BonusFormService);
  });

  describe('Service methods', () => {
    describe('createBonusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBonusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            montant: expect.any(Object),
            contrat: expect.any(Object),
          })
        );
      });

      it('passing IBonus should create a new form with FormGroup', () => {
        const formGroup = service.createBonusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            montant: expect.any(Object),
            contrat: expect.any(Object),
          })
        );
      });
    });

    describe('getBonus', () => {
      it('should return NewBonus for default Bonus initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBonusFormGroup(sampleWithNewData);

        const bonus = service.getBonus(formGroup) as any;

        expect(bonus).toMatchObject(sampleWithNewData);
      });

      it('should return NewBonus for empty Bonus initial value', () => {
        const formGroup = service.createBonusFormGroup();

        const bonus = service.getBonus(formGroup) as any;

        expect(bonus).toMatchObject({});
      });

      it('should return IBonus', () => {
        const formGroup = service.createBonusFormGroup(sampleWithRequiredData);

        const bonus = service.getBonus(formGroup) as any;

        expect(bonus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBonus should not enable id FormControl', () => {
        const formGroup = service.createBonusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBonus should disable id FormControl', () => {
        const formGroup = service.createBonusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
