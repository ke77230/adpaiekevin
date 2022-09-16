import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CotisationComponent } from '../list/cotisation.component';
import { CotisationDetailComponent } from '../detail/cotisation-detail.component';
import { CotisationUpdateComponent } from '../update/cotisation-update.component';
import { CotisationRoutingResolveService } from './cotisation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cotisationRoute: Routes = [
  {
    path: '',
    component: CotisationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CotisationDetailComponent,
    resolve: {
      cotisation: CotisationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CotisationUpdateComponent,
    resolve: {
      cotisation: CotisationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CotisationUpdateComponent,
    resolve: {
      cotisation: CotisationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cotisationRoute)],
  exports: [RouterModule],
})
export class CotisationRoutingModule {}
