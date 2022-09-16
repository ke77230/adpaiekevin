import dayjs from 'dayjs/esm';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { IEmployeur } from 'app/entities/employeur/employeur.model';
import { ITauxDImposition } from 'app/entities/taux-d-imposition/taux-d-imposition.model';
import { ICotisation } from 'app/entities/cotisation/cotisation.model';
import { IMention } from 'app/entities/mention/mention.model';

export interface IFicheDePaie {
  id: number;
  salaireBrut?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  datepaiement?: dayjs.Dayjs | null;
  salaireNet?: number | null;
  montantNetAvantImpots?: number | null;
  proFees?: number | null;
  deductions?: number | null;
  contrat?: Pick<IContrat, 'id'> | null;
  employeur?: Pick<IEmployeur, 'id'> | null;
  imposition?: Pick<ITauxDImposition, 'id'> | null;
  cotisations?: Pick<ICotisation, 'id'>[] | null;
  mentions?: Pick<IMention, 'id'>[] | null;
}

export type NewFicheDePaie = Omit<IFicheDePaie, 'id'> & { id: null };
