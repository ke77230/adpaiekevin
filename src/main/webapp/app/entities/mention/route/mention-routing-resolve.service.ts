import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMention } from '../mention.model';
import { MentionService } from '../service/mention.service';

@Injectable({ providedIn: 'root' })
export class MentionRoutingResolveService implements Resolve<IMention | null> {
  constructor(protected service: MentionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMention | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mention: HttpResponse<IMention>) => {
          if (mention.body) {
            return of(mention.body);
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
