import { IJob, NewJob } from './job.model';

export const sampleWithRequiredData: IJob = {
  id: 43395,
  jobTitle: 'b b Superviseur',
};

export const sampleWithPartialData: IJob = {
  id: 95627,
  jobTitle: 'a a Directeur',
  minSalary: 2082,
  maxSalary: 6918,
};

export const sampleWithFullData: IJob = {
  id: 4480,
  jobTitle: 'a c Executif',
  minSalary: 32907,
  maxSalary: 90392,
};

export const sampleWithNewData: NewJob = {
  jobTitle: 'c c Designer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
