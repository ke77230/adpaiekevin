import dayjs from 'dayjs/esm';
import { IFicheDePaie } from 'app/entities/fiche-de-paie/fiche-de-paie.model';
import { Categorie } from 'app/entities/enumerations/categorie.model';

export interface ICotisation {
  id: number;
  name?: string | null;
  famille?: Categorie | null;
  taux?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  actuel?: boolean | null;
  ficheDePaies?: Pick<IFicheDePaie, 'id'>[] | null;
}

export type NewCotisation = Omit<ICotisation, 'id'> & { id: null };
