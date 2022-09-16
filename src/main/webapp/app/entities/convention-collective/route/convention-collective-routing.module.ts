import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConventionCollectiveComponent } from '../list/convention-collective.component';
import { ConventionCollectiveDetailComponent } from '../detail/convention-collective-detail.component';
import { ConventionCollectiveUpdateComponent } from '../update/convention-collective-update.component';
import { ConventionCollectiveRoutingResolveService } from './convention-collective-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const conventionCollectiveRoute: Routes = [
  {
    path: '',
    component: ConventionCollectiveComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConventionCollectiveDetailComponent,
    resolve: {
      conventionCollective: ConventionCollectiveRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConventionCollectiveUpdateComponent,
    resolve: {
      conventionCollective: ConventionCollectiveRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConventionCollectiveUpdateComponent,
    resolve: {
      conventionCollective: ConventionCollectiveRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(conventionCollectiveRoute)],
  exports: [RouterModule],
})
export class ConventionCollectiveRoutingModule {}
