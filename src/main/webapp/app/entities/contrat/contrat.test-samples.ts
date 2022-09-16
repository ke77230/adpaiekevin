import dayjs from 'dayjs/esm';

import { TypeForfait } from 'app/entities/enumerations/type-forfait.model';

import { IContrat, NewContrat } from './contrat.model';

export const sampleWithRequiredData: IContrat = {
  id: 53360,
  salaireBase: 52431,
  emploi: 'Bedfordshire Rustic Frozen',
  dateArrive: dayjs('2022-09-15'),
  classification: 8724,
  typeForfait: TypeForfait['Heure'],
};

export const sampleWithPartialData: IContrat = {
  id: 44624,
  salaireBase: 18300,
  emploi: 'reciprocal Fish task-force',
  dateArrive: dayjs('2022-09-16'),
  classification: 91034,
  typeForfait: TypeForfait['Commission'],
  nbHeure: 60878,
};

export const sampleWithFullData: IContrat = {
  id: 11229,
  salaireBase: 117,
  emploi: 'Awesome hack bypass',
  dateArrive: dayjs('2022-09-16'),
  classification: 10631,
  typeForfait: TypeForfait['Jour'],
  nbHeure: 45498,
};

export const sampleWithNewData: NewContrat = {
  salaireBase: 63897,
  emploi: 'online initiatives',
  dateArrive: dayjs('2022-09-16'),
  classification: 62987,
  typeForfait: TypeForfait['Heure'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
