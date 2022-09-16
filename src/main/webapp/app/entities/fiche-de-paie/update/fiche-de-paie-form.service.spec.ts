import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fiche-de-paie.test-samples';

import { FicheDePaieFormService } from './fiche-de-paie-form.service';

describe('FicheDePaie Form Service', () => {
  let service: FicheDePaieFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FicheDePaieFormService);
  });

  describe('Service methods', () => {
    describe('createFicheDePaieFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFicheDePaieFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            salaireBrut: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            datepaiement: expect.any(Object),
            salaireNet: expect.any(Object),
            montantNetAvantImpots: expect.any(Object),
            proFees: expect.any(Object),
            deductions: expect.any(Object),
            contrat: expect.any(Object),
            employeur: expect.any(Object),
            imposition: expect.any(Object),
            cotisations: expect.any(Object),
            mentions: expect.any(Object),
          })
        );
      });

      it('passing IFicheDePaie should create a new form with FormGroup', () => {
        const formGroup = service.createFicheDePaieFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            salaireBrut: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            datepaiement: expect.any(Object),
            salaireNet: expect.any(Object),
            montantNetAvantImpots: expect.any(Object),
            proFees: expect.any(Object),
            deductions: expect.any(Object),
            contrat: expect.any(Object),
            employeur: expect.any(Object),
            imposition: expect.any(Object),
            cotisations: expect.any(Object),
            mentions: expect.any(Object),
          })
        );
      });
    });

    describe('getFicheDePaie', () => {
      it('should return NewFicheDePaie for default FicheDePaie initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFicheDePaieFormGroup(sampleWithNewData);

        const ficheDePaie = service.getFicheDePaie(formGroup) as any;

        expect(ficheDePaie).toMatchObject(sampleWithNewData);
      });

      it('should return NewFicheDePaie for empty FicheDePaie initial value', () => {
        const formGroup = service.createFicheDePaieFormGroup();

        const ficheDePaie = service.getFicheDePaie(formGroup) as any;

        expect(ficheDePaie).toMatchObject({});
      });

      it('should return IFicheDePaie', () => {
        const formGroup = service.createFicheDePaieFormGroup(sampleWithRequiredData);

        const ficheDePaie = service.getFicheDePaie(formGroup) as any;

        expect(ficheDePaie).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFicheDePaie should not enable id FormControl', () => {
        const formGroup = service.createFicheDePaieFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFicheDePaie should disable id FormControl', () => {
        const formGroup = service.createFicheDePaieFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
