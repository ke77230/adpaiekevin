import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFicheDePaie, NewFicheDePaie } from '../fiche-de-paie.model';

export type PartialUpdateFicheDePaie = Partial<IFicheDePaie> & Pick<IFicheDePaie, 'id'>;

type RestOf<T extends IFicheDePaie | NewFicheDePaie> = Omit<T, 'startDate' | 'endDate' | 'datepaiement'> & {
  startDate?: string | null;
  endDate?: string | null;
  datepaiement?: string | null;
};

export type RestFicheDePaie = RestOf<IFicheDePaie>;

export type NewRestFicheDePaie = RestOf<NewFicheDePaie>;

export type PartialUpdateRestFicheDePaie = RestOf<PartialUpdateFicheDePaie>;

export type EntityResponseType = HttpResponse<IFicheDePaie>;
export type EntityArrayResponseType = HttpResponse<IFicheDePaie[]>;

@Injectable({ providedIn: 'root' })
export class FicheDePaieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fiche-de-paies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ficheDePaie: NewFicheDePaie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ficheDePaie);
    return this.http
      .post<RestFicheDePaie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ficheDePaie: IFicheDePaie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ficheDePaie);
    return this.http
      .put<RestFicheDePaie>(`${this.resourceUrl}/${this.getFicheDePaieIdentifier(ficheDePaie)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ficheDePaie: PartialUpdateFicheDePaie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ficheDePaie);
    return this.http
      .patch<RestFicheDePaie>(`${this.resourceUrl}/${this.getFicheDePaieIdentifier(ficheDePaie)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFicheDePaie>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFicheDePaie[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFicheDePaieIdentifier(ficheDePaie: Pick<IFicheDePaie, 'id'>): number {
    return ficheDePaie.id;
  }

  compareFicheDePaie(o1: Pick<IFicheDePaie, 'id'> | null, o2: Pick<IFicheDePaie, 'id'> | null): boolean {
    return o1 && o2 ? this.getFicheDePaieIdentifier(o1) === this.getFicheDePaieIdentifier(o2) : o1 === o2;
  }

  addFicheDePaieToCollectionIfMissing<Type extends Pick<IFicheDePaie, 'id'>>(
    ficheDePaieCollection: Type[],
    ...ficheDePaiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ficheDePaies: Type[] = ficheDePaiesToCheck.filter(isPresent);
    if (ficheDePaies.length > 0) {
      const ficheDePaieCollectionIdentifiers = ficheDePaieCollection.map(
        ficheDePaieItem => this.getFicheDePaieIdentifier(ficheDePaieItem)!
      );
      const ficheDePaiesToAdd = ficheDePaies.filter(ficheDePaieItem => {
        const ficheDePaieIdentifier = this.getFicheDePaieIdentifier(ficheDePaieItem);
        if (ficheDePaieCollectionIdentifiers.includes(ficheDePaieIdentifier)) {
          return false;
        }
        ficheDePaieCollectionIdentifiers.push(ficheDePaieIdentifier);
        return true;
      });
      return [...ficheDePaiesToAdd, ...ficheDePaieCollection];
    }
    return ficheDePaieCollection;
  }

  protected convertDateFromClient<T extends IFicheDePaie | NewFicheDePaie | PartialUpdateFicheDePaie>(ficheDePaie: T): RestOf<T> {
    return {
      ...ficheDePaie,
      startDate: ficheDePaie.startDate?.format(DATE_FORMAT) ?? null,
      endDate: ficheDePaie.endDate?.format(DATE_FORMAT) ?? null,
      datepaiement: ficheDePaie.datepaiement?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFicheDePaie: RestFicheDePaie): IFicheDePaie {
    return {
      ...restFicheDePaie,
      startDate: restFicheDePaie.startDate ? dayjs(restFicheDePaie.startDate) : undefined,
      endDate: restFicheDePaie.endDate ? dayjs(restFicheDePaie.endDate) : undefined,
      datepaiement: restFicheDePaie.datepaiement ? dayjs(restFicheDePaie.datepaiement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFicheDePaie>): HttpResponse<IFicheDePaie> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFicheDePaie[]>): HttpResponse<IFicheDePaie[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
