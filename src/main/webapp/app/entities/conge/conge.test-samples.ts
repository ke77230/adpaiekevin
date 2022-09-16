import dayjs from 'dayjs/esm';

import { Decision } from 'app/entities/enumerations/decision.model';

import { IConge, NewConge } from './conge.model';

export const sampleWithRequiredData: IConge = {
  id: 46057,
  holdateStart: dayjs('2022-09-15'),
  holdateEnd: dayjs('2022-09-16'),
  holdatePay: 57126,
  nbCongePris: 26067,
  dateDemande: dayjs('2022-09-16'),
  decision: Decision['Accepte'],
};

export const sampleWithPartialData: IConge = {
  id: 77380,
  holdateStart: dayjs('2022-09-16'),
  holdateEnd: dayjs('2022-09-15'),
  holdatePay: 39481,
  nbCongePris: 20269,
  dateDemande: dayjs('2022-09-16'),
  decision: Decision['Accepte'],
  dateReponse: dayjs('2022-09-16'),
};

export const sampleWithFullData: IConge = {
  id: 19442,
  holdateStart: dayjs('2022-09-15'),
  holdateEnd: dayjs('2022-09-16'),
  holdatePay: 14342,
  nbCongeAcquis: 57902,
  nbCongePris: 37322,
  dateDemande: dayjs('2022-09-15'),
  decision: Decision['Accepte'],
  dateReponse: dayjs('2022-09-16'),
};

export const sampleWithNewData: NewConge = {
  holdateStart: dayjs('2022-09-16'),
  holdateEnd: dayjs('2022-09-15'),
  holdatePay: 70561,
  nbCongePris: 96645,
  dateDemande: dayjs('2022-09-15'),
  decision: Decision['Enattente'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
