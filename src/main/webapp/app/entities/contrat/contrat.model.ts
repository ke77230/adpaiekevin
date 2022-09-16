import dayjs from 'dayjs/esm';
import { IConventionCollective } from 'app/entities/convention-collective/convention-collective.model';
import { IEmployeur } from 'app/entities/employeur/employeur.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { TypeForfait } from 'app/entities/enumerations/type-forfait.model';

export interface IContrat {
  id: number;
  salaireBase?: number | null;
  emploi?: string | null;
  dateArrive?: dayjs.Dayjs | null;
  classification?: number | null;
  typeForfait?: TypeForfait | null;
  nbHeure?: number | null;
  conventionCollective?: Pick<IConventionCollective, 'id'> | null;
  employeur?: Pick<IEmployeur, 'id'> | null;
  employee?: Pick<IEmployee, 'id'> | null;
}

export type NewContrat = Omit<IContrat, 'id'> & { id: null };
