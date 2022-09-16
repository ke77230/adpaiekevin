import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITauxDImposition, NewTauxDImposition } from '../taux-d-imposition.model';

export type PartialUpdateTauxDImposition = Partial<ITauxDImposition> & Pick<ITauxDImposition, 'id'>;

type RestOf<T extends ITauxDImposition | NewTauxDImposition> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestTauxDImposition = RestOf<ITauxDImposition>;

export type NewRestTauxDImposition = RestOf<NewTauxDImposition>;

export type PartialUpdateRestTauxDImposition = RestOf<PartialUpdateTauxDImposition>;

export type EntityResponseType = HttpResponse<ITauxDImposition>;
export type EntityArrayResponseType = HttpResponse<ITauxDImposition[]>;

@Injectable({ providedIn: 'root' })
export class TauxDImpositionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/taux-d-impositions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tauxDImposition: NewTauxDImposition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tauxDImposition);
    return this.http
      .post<RestTauxDImposition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tauxDImposition: ITauxDImposition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tauxDImposition);
    return this.http
      .put<RestTauxDImposition>(`${this.resourceUrl}/${this.getTauxDImpositionIdentifier(tauxDImposition)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tauxDImposition: PartialUpdateTauxDImposition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tauxDImposition);
    return this.http
      .patch<RestTauxDImposition>(`${this.resourceUrl}/${this.getTauxDImpositionIdentifier(tauxDImposition)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTauxDImposition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTauxDImposition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTauxDImpositionIdentifier(tauxDImposition: Pick<ITauxDImposition, 'id'>): number {
    return tauxDImposition.id;
  }

  compareTauxDImposition(o1: Pick<ITauxDImposition, 'id'> | null, o2: Pick<ITauxDImposition, 'id'> | null): boolean {
    return o1 && o2 ? this.getTauxDImpositionIdentifier(o1) === this.getTauxDImpositionIdentifier(o2) : o1 === o2;
  }

  addTauxDImpositionToCollectionIfMissing<Type extends Pick<ITauxDImposition, 'id'>>(
    tauxDImpositionCollection: Type[],
    ...tauxDImpositionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tauxDImpositions: Type[] = tauxDImpositionsToCheck.filter(isPresent);
    if (tauxDImpositions.length > 0) {
      const tauxDImpositionCollectionIdentifiers = tauxDImpositionCollection.map(
        tauxDImpositionItem => this.getTauxDImpositionIdentifier(tauxDImpositionItem)!
      );
      const tauxDImpositionsToAdd = tauxDImpositions.filter(tauxDImpositionItem => {
        const tauxDImpositionIdentifier = this.getTauxDImpositionIdentifier(tauxDImpositionItem);
        if (tauxDImpositionCollectionIdentifiers.includes(tauxDImpositionIdentifier)) {
          return false;
        }
        tauxDImpositionCollectionIdentifiers.push(tauxDImpositionIdentifier);
        return true;
      });
      return [...tauxDImpositionsToAdd, ...tauxDImpositionCollection];
    }
    return tauxDImpositionCollection;
  }

  protected convertDateFromClient<T extends ITauxDImposition | NewTauxDImposition | PartialUpdateTauxDImposition>(
    tauxDImposition: T
  ): RestOf<T> {
    return {
      ...tauxDImposition,
      startDate: tauxDImposition.startDate?.format(DATE_FORMAT) ?? null,
      endDate: tauxDImposition.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTauxDImposition: RestTauxDImposition): ITauxDImposition {
    return {
      ...restTauxDImposition,
      startDate: restTauxDImposition.startDate ? dayjs(restTauxDImposition.startDate) : undefined,
      endDate: restTauxDImposition.endDate ? dayjs(restTauxDImposition.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTauxDImposition>): HttpResponse<ITauxDImposition> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTauxDImposition[]>): HttpResponse<ITauxDImposition[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
