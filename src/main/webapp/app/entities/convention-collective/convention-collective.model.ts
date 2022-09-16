import { IEmployeur } from 'app/entities/employeur/employeur.model';

export interface IConventionCollective {
  id: number;
  idcc?: number | null;
  nom?: string | null;
  position?: number | null;
  coefficient?: number | null;
  valeurPoint?: number | null;
  baseFixe?: number | null;
  salaireMinimaux?: number | null;
  employeurs?: Pick<IEmployeur, 'id'>[] | null;
}

export type NewConventionCollective = Omit<IConventionCollective, 'id'> & { id: null };
