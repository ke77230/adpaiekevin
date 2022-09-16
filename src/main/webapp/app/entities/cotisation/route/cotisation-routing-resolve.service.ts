import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICotisation } from '../cotisation.model';
import { CotisationService } from '../service/cotisation.service';

@Injectable({ providedIn: 'root' })
export class CotisationRoutingResolveService implements Resolve<ICotisation | null> {
  constructor(protected service: CotisationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICotisation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cotisation: HttpResponse<ICotisation>) => {
          if (cotisation.body) {
            return of(cotisation.body);
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
