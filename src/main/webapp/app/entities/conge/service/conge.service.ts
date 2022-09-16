import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConge, NewConge } from '../conge.model';

export type PartialUpdateConge = Partial<IConge> & Pick<IConge, 'id'>;

type RestOf<T extends IConge | NewConge> = Omit<T, 'holdateStart' | 'holdateEnd' | 'dateDemande' | 'dateReponse'> & {
  holdateStart?: string | null;
  holdateEnd?: string | null;
  dateDemande?: string | null;
  dateReponse?: string | null;
};

export type RestConge = RestOf<IConge>;

export type NewRestConge = RestOf<NewConge>;

export type PartialUpdateRestConge = RestOf<PartialUpdateConge>;

export type EntityResponseType = HttpResponse<IConge>;
export type EntityArrayResponseType = HttpResponse<IConge[]>;

@Injectable({ providedIn: 'root' })
export class CongeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(conge: NewConge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conge);
    return this.http.post<RestConge>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(conge: IConge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conge);
    return this.http
      .put<RestConge>(`${this.resourceUrl}/${this.getCongeIdentifier(conge)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(conge: PartialUpdateConge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conge);
    return this.http
      .patch<RestConge>(`${this.resourceUrl}/${this.getCongeIdentifier(conge)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestConge>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConge[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCongeIdentifier(conge: Pick<IConge, 'id'>): number {
    return conge.id;
  }

  compareConge(o1: Pick<IConge, 'id'> | null, o2: Pick<IConge, 'id'> | null): boolean {
    return o1 && o2 ? this.getCongeIdentifier(o1) === this.getCongeIdentifier(o2) : o1 === o2;
  }

  addCongeToCollectionIfMissing<Type extends Pick<IConge, 'id'>>(
    congeCollection: Type[],
    ...congesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const conges: Type[] = congesToCheck.filter(isPresent);
    if (conges.length > 0) {
      const congeCollectionIdentifiers = congeCollection.map(congeItem => this.getCongeIdentifier(congeItem)!);
      const congesToAdd = conges.filter(congeItem => {
        const congeIdentifier = this.getCongeIdentifier(congeItem);
        if (congeCollectionIdentifiers.includes(congeIdentifier)) {
          return false;
        }
        congeCollectionIdentifiers.push(congeIdentifier);
        return true;
      });
      return [...congesToAdd, ...congeCollection];
    }
    return congeCollection;
  }

  protected convertDateFromClient<T extends IConge | NewConge | PartialUpdateConge>(conge: T): RestOf<T> {
    return {
      ...conge,
      holdateStart: conge.holdateStart?.format(DATE_FORMAT) ?? null,
      holdateEnd: conge.holdateEnd?.format(DATE_FORMAT) ?? null,
      dateDemande: conge.dateDemande?.format(DATE_FORMAT) ?? null,
      dateReponse: conge.dateReponse?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restConge: RestConge): IConge {
    return {
      ...restConge,
      holdateStart: restConge.holdateStart ? dayjs(restConge.holdateStart) : undefined,
      holdateEnd: restConge.holdateEnd ? dayjs(restConge.holdateEnd) : undefined,
      dateDemande: restConge.dateDemande ? dayjs(restConge.dateDemande) : undefined,
      dateReponse: restConge.dateReponse ? dayjs(restConge.dateReponse) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestConge>): HttpResponse<IConge> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestConge[]>): HttpResponse<IConge[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
