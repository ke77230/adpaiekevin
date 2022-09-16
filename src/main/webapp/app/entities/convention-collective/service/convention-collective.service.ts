import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConventionCollective, NewConventionCollective } from '../convention-collective.model';

export type PartialUpdateConventionCollective = Partial<IConventionCollective> & Pick<IConventionCollective, 'id'>;

export type EntityResponseType = HttpResponse<IConventionCollective>;
export type EntityArrayResponseType = HttpResponse<IConventionCollective[]>;

@Injectable({ providedIn: 'root' })
export class ConventionCollectiveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/convention-collectives');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(conventionCollective: NewConventionCollective): Observable<EntityResponseType> {
    return this.http.post<IConventionCollective>(this.resourceUrl, conventionCollective, { observe: 'response' });
  }

  update(conventionCollective: IConventionCollective): Observable<EntityResponseType> {
    return this.http.put<IConventionCollective>(
      `${this.resourceUrl}/${this.getConventionCollectiveIdentifier(conventionCollective)}`,
      conventionCollective,
      { observe: 'response' }
    );
  }

  partialUpdate(conventionCollective: PartialUpdateConventionCollective): Observable<EntityResponseType> {
    return this.http.patch<IConventionCollective>(
      `${this.resourceUrl}/${this.getConventionCollectiveIdentifier(conventionCollective)}`,
      conventionCollective,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConventionCollective>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConventionCollective[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConventionCollectiveIdentifier(conventionCollective: Pick<IConventionCollective, 'id'>): number {
    return conventionCollective.id;
  }

  compareConventionCollective(o1: Pick<IConventionCollective, 'id'> | null, o2: Pick<IConventionCollective, 'id'> | null): boolean {
    return o1 && o2 ? this.getConventionCollectiveIdentifier(o1) === this.getConventionCollectiveIdentifier(o2) : o1 === o2;
  }

  addConventionCollectiveToCollectionIfMissing<Type extends Pick<IConventionCollective, 'id'>>(
    conventionCollectiveCollection: Type[],
    ...conventionCollectivesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const conventionCollectives: Type[] = conventionCollectivesToCheck.filter(isPresent);
    if (conventionCollectives.length > 0) {
      const conventionCollectiveCollectionIdentifiers = conventionCollectiveCollection.map(
        conventionCollectiveItem => this.getConventionCollectiveIdentifier(conventionCollectiveItem)!
      );
      const conventionCollectivesToAdd = conventionCollectives.filter(conventionCollectiveItem => {
        const conventionCollectiveIdentifier = this.getConventionCollectiveIdentifier(conventionCollectiveItem);
        if (conventionCollectiveCollectionIdentifiers.includes(conventionCollectiveIdentifier)) {
          return false;
        }
        conventionCollectiveCollectionIdentifiers.push(conventionCollectiveIdentifier);
        return true;
      });
      return [...conventionCollectivesToAdd, ...conventionCollectiveCollection];
    }
    return conventionCollectiveCollection;
  }
}
