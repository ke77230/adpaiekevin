import dayjs from 'dayjs/esm';

import { ITauxDImposition, NewTauxDImposition } from './taux-d-imposition.model';

export const sampleWithRequiredData: ITauxDImposition = {
  id: 86772,
  taux: 85898,
  minSalary: 97287,
  startDate: dayjs('2022-09-16'),
};

export const sampleWithPartialData: ITauxDImposition = {
  id: 77034,
  taux: 25970,
  minSalary: 26929,
  startDate: dayjs('2022-09-16'),
  endDate: dayjs('2022-09-15'),
};

export const sampleWithFullData: ITauxDImposition = {
  id: 1116,
  taux: 80357,
  minSalary: 41383,
  maxSalary: 11208,
  startDate: dayjs('2022-09-16'),
  endDate: dayjs('2022-09-15'),
};

export const sampleWithNewData: NewTauxDImposition = {
  taux: 47041,
  minSalary: 78747,
  startDate: dayjs('2022-09-15'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
