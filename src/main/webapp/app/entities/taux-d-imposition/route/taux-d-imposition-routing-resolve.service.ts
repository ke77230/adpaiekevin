import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITauxDImposition } from '../taux-d-imposition.model';
import { TauxDImpositionService } from '../service/taux-d-imposition.service';

@Injectable({ providedIn: 'root' })
export class TauxDImpositionRoutingResolveService implements Resolve<ITauxDImposition | null> {
  constructor(protected service: TauxDImpositionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITauxDImposition | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tauxDImposition: HttpResponse<ITauxDImposition>) => {
          if (tauxDImposition.body) {
            return of(tauxDImposition.body);
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
