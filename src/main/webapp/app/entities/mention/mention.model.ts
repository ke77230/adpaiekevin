import { IFicheDePaie } from 'app/entities/fiche-de-paie/fiche-de-paie.model';

export interface IMention {
  id: number;
  mention?: string | null;
  ficheDePaies?: Pick<IFicheDePaie, 'id'>[] | null;
}

export type NewMention = Omit<IMention, 'id'> & { id: null };
