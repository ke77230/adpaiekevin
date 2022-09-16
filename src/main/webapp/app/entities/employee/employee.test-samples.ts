import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 7813,
  firstName: 'Francine',
  lastName: 'Le roux',
  numeroSecuriteSociale: 'Chicken reboot ',
};

export const sampleWithPartialData: IEmployee = {
  id: 8063,
  firstName: 'Aurélie',
  lastName: 'Dumas',
  numeroSecuriteSociale: 'empower solutio',
};

export const sampleWithFullData: IEmployee = {
  id: 44612,
  firstName: 'Taurin',
  lastName: 'Garnier',
  numeroSecuriteSociale: 'Pizza paradigmX',
  qualification: 'c invoice Berkshire',
  tauxImposition: 95971,
};

export const sampleWithNewData: NewEmployee = {
  firstName: 'Marion',
  lastName: 'Nguyen',
  numeroSecuriteSociale: 'Rubber Fundamen',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
