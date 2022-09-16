import dayjs from 'dayjs/esm';

export interface ITauxDImposition {
  id: number;
  taux?: number | null;
  minSalary?: number | null;
  maxSalary?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
}

export type NewTauxDImposition = Omit<ITauxDImposition, 'id'> & { id: null };
