import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../conge.test-samples';

import { CongeFormService } from './conge-form.service';

describe('Conge Form Service', () => {
  let service: CongeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CongeFormService);
  });

  describe('Service methods', () => {
    describe('createCongeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCongeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            holdateStart: expect.any(Object),
            holdateEnd: expect.any(Object),
            holdatePay: expect.any(Object),
            nbCongeAcquis: expect.any(Object),
            nbCongePris: expect.any(Object),
            dateDemande: expect.any(Object),
            decision: expect.any(Object),
            dateReponse: expect.any(Object),
            contrat: expect.any(Object),
          })
        );
      });

      it('passing IConge should create a new form with FormGroup', () => {
        const formGroup = service.createCongeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            holdateStart: expect.any(Object),
            holdateEnd: expect.any(Object),
            holdatePay: expect.any(Object),
            nbCongeAcquis: expect.any(Object),
            nbCongePris: expect.any(Object),
            dateDemande: expect.any(Object),
            decision: expect.any(Object),
            dateReponse: expect.any(Object),
            contrat: expect.any(Object),
          })
        );
      });
    });

    describe('getConge', () => {
      it('should return NewConge for default Conge initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCongeFormGroup(sampleWithNewData);

        const conge = service.getConge(formGroup) as any;

        expect(conge).toMatchObject(sampleWithNewData);
      });

      it('should return NewConge for empty Conge initial value', () => {
        const formGroup = service.createCongeFormGroup();

        const conge = service.getConge(formGroup) as any;

        expect(conge).toMatchObject({});
      });

      it('should return IConge', () => {
        const formGroup = service.createCongeFormGroup(sampleWithRequiredData);

        const conge = service.getConge(formGroup) as any;

        expect(conge).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConge should not enable id FormControl', () => {
        const formGroup = service.createCongeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConge should disable id FormControl', () => {
        const formGroup = service.createCongeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
