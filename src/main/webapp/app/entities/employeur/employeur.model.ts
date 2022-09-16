import { IConventionCollective } from 'app/entities/convention-collective/convention-collective.model';
import { ILocation } from 'app/entities/location/location.model';

export interface IEmployeur {
  id: number;
  name?: string | null;
  numeroSiret?: number | null;
  numApe?: number | null;
  numUrssaf?: number | null;
  conventionCollectives?: Pick<IConventionCollective, 'id'>[] | null;
  locations?: Pick<ILocation, 'id'>[] | null;
}

export type NewEmployeur = Omit<IEmployeur, 'id'> & { id: null };
