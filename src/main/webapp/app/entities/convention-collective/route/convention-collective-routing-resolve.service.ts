import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConventionCollective } from '../convention-collective.model';
import { ConventionCollectiveService } from '../service/convention-collective.service';

@Injectable({ providedIn: 'root' })
export class ConventionCollectiveRoutingResolveService implements Resolve<IConventionCollective | null> {
  constructor(protected service: ConventionCollectiveService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConventionCollective | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((conventionCollective: HttpResponse<IConventionCollective>) => {
          if (conventionCollective.body) {
            return of(conventionCollective.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
