import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employeur.test-samples';

import { EmployeurFormService } from './employeur-form.service';

describe('Employeur Form Service', () => {
  let service: EmployeurFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeurFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            numeroSiret: expect.any(Object),
            numApe: expect.any(Object),
            numUrssaf: expect.any(Object),
            conventionCollectives: expect.any(Object),
            locations: expect.any(Object),
          })
        );
      });

      it('passing IEmployeur should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            numeroSiret: expect.any(Object),
            numApe: expect.any(Object),
            numUrssaf: expect.any(Object),
            conventionCollectives: expect.any(Object),
            locations: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeur', () => {
      it('should return NewEmployeur for default Employeur initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeurFormGroup(sampleWithNewData);

        const employeur = service.getEmployeur(formGroup) as any;

        expect(employeur).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeur for empty Employeur initial value', () => {
        const formGroup = service.createEmployeurFormGroup();

        const employeur = service.getEmployeur(formGroup) as any;

        expect(employeur).toMatchObject({});
      });

      it('should return IEmployeur', () => {
        const formGroup = service.createEmployeurFormGroup(sampleWithRequiredData);

        const employeur = service.getEmployeur(formGroup) as any;

        expect(employeur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeur should not enable id FormControl', () => {
        const formGroup = service.createEmployeurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeur should disable id FormControl', () => {
        const formGroup = service.createEmployeurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
