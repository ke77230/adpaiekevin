import { IEmployee } from 'app/entities/employee/employee.model';
import { IEmployeur } from 'app/entities/employeur/employeur.model';

export interface ILocation {
  id: number;
  streetName?: string | null;
  numeroRue?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  employees?: Pick<IEmployee, 'id'>[] | null;
  employeurs?: Pick<IEmployeur, 'id'>[] | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
