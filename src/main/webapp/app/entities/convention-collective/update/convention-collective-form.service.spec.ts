import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../convention-collective.test-samples';

import { ConventionCollectiveFormService } from './convention-collective-form.service';

describe('ConventionCollective Form Service', () => {
  let service: ConventionCollectiveFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConventionCollectiveFormService);
  });

  describe('Service methods', () => {
    describe('createConventionCollectiveFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConventionCollectiveFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idcc: expect.any(Object),
            nom: expect.any(Object),
            position: expect.any(Object),
            coefficient: expect.any(Object),
            valeurPoint: expect.any(Object),
            baseFixe: expect.any(Object),
            salaireMinimaux: expect.any(Object),
            employeurs: expect.any(Object),
          })
        );
      });

      it('passing IConventionCollective should create a new form with FormGroup', () => {
        const formGroup = service.createConventionCollectiveFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idcc: expect.any(Object),
            nom: expect.any(Object),
            position: expect.any(Object),
            coefficient: expect.any(Object),
            valeurPoint: expect.any(Object),
            baseFixe: expect.any(Object),
            salaireMinimaux: expect.any(Object),
            employeurs: expect.any(Object),
          })
        );
      });
    });

    describe('getConventionCollective', () => {
      it('should return NewConventionCollective for default ConventionCollective initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createConventionCollectiveFormGroup(sampleWithNewData);

        const conventionCollective = service.getConventionCollective(formGroup) as any;

        expect(conventionCollective).toMatchObject(sampleWithNewData);
      });

      it('should return NewConventionCollective for empty ConventionCollective initial value', () => {
        const formGroup = service.createConventionCollectiveFormGroup();

        const conventionCollective = service.getConventionCollective(formGroup) as any;

        expect(conventionCollective).toMatchObject({});
      });

      it('should return IConventionCollective', () => {
        const formGroup = service.createConventionCollectiveFormGroup(sampleWithRequiredData);

        const conventionCollective = service.getConventionCollective(formGroup) as any;

        expect(conventionCollective).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConventionCollective should not enable id FormControl', () => {
        const formGroup = service.createConventionCollectiveFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConventionCollective should disable id FormControl', () => {
        const formGroup = service.createConventionCollectiveFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
