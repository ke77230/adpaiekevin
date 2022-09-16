import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeur } from '../employeur.model';
import { EmployeurService } from '../service/employeur.service';

@Injectable({ providedIn: 'root' })
export class EmployeurRoutingResolveService implements Resolve<IEmployeur | null> {
  constructor(protected service: EmployeurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeur | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeur: HttpResponse<IEmployeur>) => {
          if (employeur.body) {
            return of(employeur.body);
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
