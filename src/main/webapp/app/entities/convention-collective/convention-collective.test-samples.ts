import { IConventionCollective, NewConventionCollective } from './convention-collective.model';

export const sampleWithRequiredData: IConventionCollective = {
  id: 47114,
  idcc: 49004,
  nom: 'Lek state Kids',
  position: 44982,
  coefficient: 3255,
  valeurPoint: 92699,
  baseFixe: 77200,
  salaireMinimaux: 91910,
};

export const sampleWithPartialData: IConventionCollective = {
  id: 90237,
  idcc: 50547,
  nom: 'neutral c',
  position: 91783,
  coefficient: 67741,
  valeurPoint: 50141,
  baseFixe: 29282,
  salaireMinimaux: 67802,
};

export const sampleWithFullData: IConventionCollective = {
  id: 78208,
  idcc: 65351,
  nom: 'concept Huchette Loan',
  position: 66373,
  coefficient: 23244,
  valeurPoint: 91633,
  baseFixe: 69258,
  salaireMinimaux: 1829,
};

export const sampleWithNewData: NewConventionCollective = {
  idcc: 53950,
  nom: 'Plastic feed port',
  position: 31049,
  coefficient: 11944,
  valeurPoint: 45199,
  baseFixe: 94541,
  salaireMinimaux: 32601,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
