import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cotisation.test-samples';

import { CotisationFormService } from './cotisation-form.service';

describe('Cotisation Form Service', () => {
  let service: CotisationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CotisationFormService);
  });

  describe('Service methods', () => {
    describe('createCotisationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCotisationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            famille: expect.any(Object),
            taux: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            actuel: expect.any(Object),
            ficheDePaies: expect.any(Object),
          })
        );
      });

      it('passing ICotisation should create a new form with FormGroup', () => {
        const formGroup = service.createCotisationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            famille: expect.any(Object),
            taux: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            actuel: expect.any(Object),
            ficheDePaies: expect.any(Object),
          })
        );
      });
    });

    describe('getCotisation', () => {
      it('should return NewCotisation for default Cotisation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCotisationFormGroup(sampleWithNewData);

        const cotisation = service.getCotisation(formGroup) as any;

        expect(cotisation).toMatchObject(sampleWithNewData);
      });

      it('should return NewCotisation for empty Cotisation initial value', () => {
        const formGroup = service.createCotisationFormGroup();

        const cotisation = service.getCotisation(formGroup) as any;

        expect(cotisation).toMatchObject({});
      });

      it('should return ICotisation', () => {
        const formGroup = service.createCotisationFormGroup(sampleWithRequiredData);

        const cotisation = service.getCotisation(formGroup) as any;

        expect(cotisation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICotisation should not enable id FormControl', () => {
        const formGroup = service.createCotisationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCotisation should disable id FormControl', () => {
        const formGroup = service.createCotisationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
