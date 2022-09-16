import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConge } from '../conge.model';
import { CongeService } from '../service/conge.service';

@Injectable({ providedIn: 'root' })
export class CongeRoutingResolveService implements Resolve<IConge | null> {
  constructor(protected service: CongeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConge | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((conge: HttpResponse<IConge>) => {
          if (conge.body) {
            return of(conge.body);
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
