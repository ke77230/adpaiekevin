import { IJob } from 'app/entities/job/job.model';
import { ILocation } from 'app/entities/location/location.model';

export interface IEmployee {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  numeroSecuriteSociale?: string | null;
  qualification?: string | null;
  tauxImposition?: number | null;
  employee?: Pick<IEmployee, 'id'> | null;
  jobs?: Pick<IJob, 'id'>[] | null;
  locations?: Pick<ILocation, 'id'>[] | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
