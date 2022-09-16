import { IBonus, NewBonus } from './bonus.model';

export const sampleWithRequiredData: IBonus = {
  id: 30279,
  nom: 'payment',
  montant: 30899,
};

export const sampleWithPartialData: IBonus = {
  id: 6124,
  nom: 'PCI',
  montant: 85465,
};

export const sampleWithFullData: IBonus = {
  id: 45254,
  nom: 'solution-oriented',
  montant: 86126,
};

export const sampleWithNewData: NewBonus = {
  nom: 'teal Tools',
  montant: 21300,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
