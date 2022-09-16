import dayjs from 'dayjs/esm';

import { Categorie } from 'app/entities/enumerations/categorie.model';

import { ICotisation, NewCotisation } from './cotisation.model';

export const sampleWithRequiredData: ICotisation = {
  id: 93164,
  name: 'Account panel',
  famille: Categorie['ReductionCotisationsPatronales'],
  taux: 58278,
  startDate: dayjs('2022-09-16'),
  actuel: true,
};

export const sampleWithPartialData: ICotisation = {
  id: 21244,
  name: 'Concrete solution-oriented',
  famille: Categorie['AutresCotisationsEmployeur'],
  taux: 21367,
  startDate: dayjs('2022-09-16'),
  actuel: true,
};

export const sampleWithFullData: ICotisation = {
  id: 38485,
  name: 'Shirt Centralized',
  famille: Categorie['Famille'],
  taux: 31449,
  startDate: dayjs('2022-09-16'),
  endDate: dayjs('2022-09-16'),
  actuel: false,
};

export const sampleWithNewData: NewCotisation = {
  name: 'services',
  famille: Categorie['Famille'],
  taux: 41194,
  startDate: dayjs('2022-09-16'),
  actuel: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
