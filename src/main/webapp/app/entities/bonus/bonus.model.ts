import { IContrat } from 'app/entities/contrat/contrat.model';

export interface IBonus {
  id: number;
  nom?: string | null;
  montant?: number | null;
  contrat?: Pick<IContrat, 'id'> | null;
}

export type NewBonus = Omit<IBonus, 'id'> & { id: null };
