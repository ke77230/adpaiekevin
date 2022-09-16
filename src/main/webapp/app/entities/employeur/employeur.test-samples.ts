import { IEmployeur, NewEmployeur } from './employeur.model';

export const sampleWithRequiredData: IEmployeur = {
  id: 10965,
  name: 'Frozen markets adapter',
  numeroSiret: 14,
  numApe: 5,
  numUrssaf: 14,
};

export const sampleWithPartialData: IEmployeur = {
  id: 36355,
  name: 'leverage quantifying Ingenieur',
  numeroSiret: 14,
  numApe: 5,
  numUrssaf: 14,
};

export const sampleWithFullData: IEmployeur = {
  id: 74039,
  name: 'Refined ivory',
  numeroSiret: 14,
  numApe: 5,
  numUrssaf: 14,
};

export const sampleWithNewData: NewEmployeur = {
  name: 'Fantastic Ingenieur',
  numeroSiret: 14,
  numApe: 5,
  numUrssaf: 14,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
