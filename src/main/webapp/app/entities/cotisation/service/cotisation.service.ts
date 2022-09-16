import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICotisation, NewCotisation } from '../cotisation.model';

export type PartialUpdateCotisation = Partial<ICotisation> & Pick<ICotisation, 'id'>;

type RestOf<T extends ICotisation | NewCotisation> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestCotisation = RestOf<ICotisation>;

export type NewRestCotisation = RestOf<NewCotisation>;

export type PartialUpdateRestCotisation = RestOf<PartialUpdateCotisation>;

export type EntityResponseType = HttpResponse<ICotisation>;
export type EntityArrayResponseType = HttpResponse<ICotisation[]>;

@Injectable({ providedIn: 'root' })
export class CotisationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cotisations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cotisation: NewCotisation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cotisation);
    return this.http
      .post<RestCotisation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cotisation: ICotisation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cotisation);
    return this.http
      .put<RestCotisation>(`${this.resourceUrl}/${this.getCotisationIdentifier(cotisation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cotisation: PartialUpdateCotisation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cotisation);
    return this.http
      .patch<RestCotisation>(`${this.resourceUrl}/${this.getCotisationIdentifier(cotisation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCotisation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCotisation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCotisationIdentifier(cotisation: Pick<ICotisation, 'id'>): number {
    return cotisation.id;
  }

  compareCotisation(o1: Pick<ICotisation, 'id'> | null, o2: Pick<ICotisation, 'id'> | null): boolean {
    return o1 && o2 ? this.getCotisationIdentifier(o1) === this.getCotisationIdentifier(o2) : o1 === o2;
  }

  addCotisationToCollectionIfMissing<Type extends Pick<ICotisation, 'id'>>(
    cotisationCollection: Type[],
    ...cotisationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cotisations: Type[] = cotisationsToCheck.filter(isPresent);
    if (cotisations.length > 0) {
      const cotisationCollectionIdentifiers = cotisationCollection.map(cotisationItem => this.getCotisationIdentifier(cotisationItem)!);
      const cotisationsToAdd = cotisations.filter(cotisationItem => {
        const cotisationIdentifier = this.getCotisationIdentifier(cotisationItem);
        if (cotisationCollectionIdentifiers.includes(cotisationIdentifier)) {
          return false;
        }
        cotisationCollectionIdentifiers.push(cotisationIdentifier);
        return true;
      });
      return [...cotisationsToAdd, ...cotisationCollection];
    }
    return cotisationCollection;
  }

  protected convertDateFromClient<T extends ICotisation | NewCotisation | PartialUpdateCotisation>(cotisation: T): RestOf<T> {
    return {
      ...cotisation,
      startDate: cotisation.startDate?.format(DATE_FORMAT) ?? null,
      endDate: cotisation.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCotisation: RestCotisation): ICotisation {
    return {
      ...restCotisation,
      startDate: restCotisation.startDate ? dayjs(restCotisation.startDate) : undefined,
      endDate: restCotisation.endDate ? dayjs(restCotisation.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCotisation>): HttpResponse<ICotisation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCotisation[]>): HttpResponse<ICotisation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
