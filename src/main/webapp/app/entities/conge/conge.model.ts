import dayjs from 'dayjs/esm';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { Decision } from 'app/entities/enumerations/decision.model';

export interface IConge {
  id: number;
  holdateStart?: dayjs.Dayjs | null;
  holdateEnd?: dayjs.Dayjs | null;
  holdatePay?: number | null;
  nbCongeAcquis?: number | null;
  nbCongePris?: number | null;
  dateDemande?: dayjs.Dayjs | null;
  decision?: Decision | null;
  dateReponse?: dayjs.Dayjs | null;
  contrat?: Pick<IContrat, 'id'> | null;
}

export type NewConge = Omit<IConge, 'id'> & { id: null };
