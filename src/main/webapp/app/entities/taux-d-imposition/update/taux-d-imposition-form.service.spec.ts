import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../taux-d-imposition.test-samples';

import { TauxDImpositionFormService } from './taux-d-imposition-form.service';

describe('TauxDImposition Form Service', () => {
  let service: TauxDImpositionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TauxDImpositionFormService);
  });

  describe('Service methods', () => {
    describe('createTauxDImpositionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTauxDImpositionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            taux: expect.any(Object),
            minSalary: expect.any(Object),
            maxSalary: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
          })
        );
      });

      it('passing ITauxDImposition should create a new form with FormGroup', () => {
        const formGroup = service.createTauxDImpositionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            taux: expect.any(Object),
            minSalary: expect.any(Object),
            maxSalary: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
          })
        );
      });
    });

    describe('getTauxDImposition', () => {
      it('should return NewTauxDImposition for default TauxDImposition initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTauxDImpositionFormGroup(sampleWithNewData);

        const tauxDImposition = service.getTauxDImposition(formGroup) as any;

        expect(tauxDImposition).toMatchObject(sampleWithNewData);
      });

      it('should return NewTauxDImposition for empty TauxDImposition initial value', () => {
        const formGroup = service.createTauxDImpositionFormGroup();

        const tauxDImposition = service.getTauxDImposition(formGroup) as any;

        expect(tauxDImposition).toMatchObject({});
      });

      it('should return ITauxDImposition', () => {
        const formGroup = service.createTauxDImpositionFormGroup(sampleWithRequiredData);

        const tauxDImposition = service.getTauxDImposition(formGroup) as any;

        expect(tauxDImposition).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITauxDImposition should not enable id FormControl', () => {
        const formGroup = service.createTauxDImpositionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTauxDImposition should disable id FormControl', () => {
        const formGroup = service.createTauxDImpositionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
