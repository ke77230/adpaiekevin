import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TauxDImpositionComponent } from '../list/taux-d-imposition.component';
import { TauxDImpositionDetailComponent } from '../detail/taux-d-imposition-detail.component';
import { TauxDImpositionUpdateComponent } from '../update/taux-d-imposition-update.component';
import { TauxDImpositionRoutingResolveService } from './taux-d-imposition-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tauxDImpositionRoute: Routes = [
  {
    path: '',
    component: TauxDImpositionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TauxDImpositionDetailComponent,
    resolve: {
      tauxDImposition: TauxDImpositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TauxDImpositionUpdateComponent,
    resolve: {
      tauxDImposition: TauxDImpositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TauxDImpositionUpdateComponent,
    resolve: {
      tauxDImposition: TauxDImpositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tauxDImpositionRoute)],
  exports: [RouterModule],
})
export class TauxDImpositionRoutingModule {}
