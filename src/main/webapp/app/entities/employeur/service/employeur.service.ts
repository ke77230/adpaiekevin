import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeur, NewEmployeur } from '../employeur.model';

export type PartialUpdateEmployeur = Partial<IEmployeur> & Pick<IEmployeur, 'id'>;

export type EntityResponseType = HttpResponse<IEmployeur>;
export type EntityArrayResponseType = HttpResponse<IEmployeur[]>;

@Injectable({ providedIn: 'root' })
export class EmployeurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employeurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeur: NewEmployeur): Observable<EntityResponseType> {
    return this.http.post<IEmployeur>(this.resourceUrl, employeur, { observe: 'response' });
  }

  update(employeur: IEmployeur): Observable<EntityResponseType> {
    return this.http.put<IEmployeur>(`${this.resourceUrl}/${this.getEmployeurIdentifier(employeur)}`, employeur, { observe: 'response' });
  }

  partialUpdate(employeur: PartialUpdateEmployeur): Observable<EntityResponseType> {
    return this.http.patch<IEmployeur>(`${this.resourceUrl}/${this.getEmployeurIdentifier(employeur)}`, employeur, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeurIdentifier(employeur: Pick<IEmployeur, 'id'>): number {
    return employeur.id;
  }

  compareEmployeur(o1: Pick<IEmployeur, 'id'> | null, o2: Pick<IEmployeur, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeurIdentifier(o1) === this.getEmployeurIdentifier(o2) : o1 === o2;
  }

  addEmployeurToCollectionIfMissing<Type extends Pick<IEmployeur, 'id'>>(
    employeurCollection: Type[],
    ...employeursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeurs: Type[] = employeursToCheck.filter(isPresent);
    if (employeurs.length > 0) {
      const employeurCollectionIdentifiers = employeurCollection.map(employeurItem => this.getEmployeurIdentifier(employeurItem)!);
      const employeursToAdd = employeurs.filter(employeurItem => {
        const employeurIdentifier = this.getEmployeurIdentifier(employeurItem);
        if (employeurCollectionIdentifiers.includes(employeurIdentifier)) {
          return false;
        }
        employeurCollectionIdentifiers.push(employeurIdentifier);
        return true;
      });
      return [...employeursToAdd, ...employeurCollection];
    }
    return employeurCollection;
  }
}
