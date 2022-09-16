import { IEmployee } from 'app/entities/employee/employee.model';

export interface IJob {
  id: number;
  jobTitle?: string | null;
  minSalary?: number | null;
  maxSalary?: number | null;
  employees?: Pick<IEmployee, 'id'>[] | null;
}

export type NewJob = Omit<IJob, 'id'> & { id: null };
