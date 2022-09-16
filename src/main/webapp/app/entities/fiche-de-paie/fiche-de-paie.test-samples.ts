import dayjs from 'dayjs/esm';

import { IFicheDePaie, NewFicheDePaie } from './fiche-de-paie.model';

export const sampleWithRequiredData: IFicheDePaie = {
  id: 39473,
  salaireBrut: 94896,
  startDate: dayjs('2022-09-16'),
  endDate: dayjs('2022-09-16'),
  datepaiement: dayjs('2022-09-15'),
  salaireNet: 86787,
  montantNetAvantImpots: 734,
  proFees: 9120,
};

export const sampleWithPartialData: IFicheDePaie = {
  id: 64537,
  salaireBrut: 62674,
  startDate: dayjs('2022-09-16'),
  endDate: dayjs('2022-09-16'),
  datepaiement: dayjs('2022-09-16'),
  salaireNet: 33751,
  montantNetAvantImpots: 86907,
  proFees: 26534,
  deductions: 78769,
};

export const sampleWithFullData: IFicheDePaie = {
  id: 96525,
  salaireBrut: 46847,
  startDate: dayjs('2022-09-15'),
  endDate: dayjs('2022-09-16'),
  datepaiement: dayjs('2022-09-15'),
  salaireNet: 50727,
  montantNetAvantImpots: 10330,
  proFees: 35014,
  deductions: 86829,
};

export const sampleWithNewData: NewFicheDePaie = {
  salaireBrut: 34518,
  startDate: dayjs('2022-09-15'),
  endDate: dayjs('2022-09-16'),
  datepaiement: dayjs('2022-09-16'),
  salaireNet: 73724,
  montantNetAvantImpots: 91425,
  proFees: 51928,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
