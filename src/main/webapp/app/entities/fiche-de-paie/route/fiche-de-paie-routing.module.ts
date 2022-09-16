import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FicheDePaieComponent } from '../list/fiche-de-paie.component';
import { FicheDePaieDetailComponent } from '../detail/fiche-de-paie-detail.component';
import { FicheDePaieUpdateComponent } from '../update/fiche-de-paie-update.component';
import { FicheDePaieRoutingResolveService } from './fiche-de-paie-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ficheDePaieRoute: Routes = [
  {
    path: '',
    component: FicheDePaieComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FicheDePaieDetailComponent,
    resolve: {
      ficheDePaie: FicheDePaieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FicheDePaieUpdateComponent,
    resolve: {
      ficheDePaie: FicheDePaieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FicheDePaieUpdateComponent,
    resolve: {
      ficheDePaie: FicheDePaieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ficheDePaieRoute)],
  exports: [RouterModule],
})
export class FicheDePaieRoutingModule {}
